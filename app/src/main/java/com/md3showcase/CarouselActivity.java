package com.md3showcase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * CarouselActivity
 *
 * Demonstrates:
 * 1. Material Carousel — horizontal multi-browse RecyclerView with
 *    scaled peek items and snap-to behaviour.
 * 2. Stacked Cards — z-axis overlapping cards with swipe-left/right
 *    gesture detection to dismiss the top card (wallet / task-switcher style).
 * 3. Skeleton / Shimmer Loaders — programmatically drawn moving light
 *    gradient over placeholder shapes while data is "loading".
 * 4. Collapsing Toolbar + Parallax — see MainActivity; info card here
 *    directs user back.
 */
public class CarouselActivity extends AppCompatActivity {

    // ── Carousel ──
    private RecyclerView carouselRecycler;
    private CarouselAdapter carouselAdapter;

    // ── Stacked cards ──
    private FrameLayout stackedCardsContainer;
    private List<StackedCardData> stackData = new ArrayList<>();
    private int[] cardColors;
    private int topCardIndex = 0;
    private static final int CARD_COUNT = 5;
    private static final float CARD_OFFSET_DP = 10f;
    private static final float CARD_SCALE_STEP = 0.04f;

    // ── Shimmer ──
    private LinearLayout shimmerContainer;
    private boolean shimmerRunning = true;
    private Handler shimmerHandler = new Handler(Looper.getMainLooper());
    private ValueAnimator shimmerAnimator;

    // ── Root view for Snackbar ──
    private View rootView;

    // ─────────────────────────────────────────────────────────────────────────
    //  Lifecycle
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        rootView = findViewById(android.R.id.content);

        setupToolbar();
        setupCarousel();
        setupStackedCards();
        setupShimmer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shimmerRunning) startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopShimmerAnimation();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Toolbar
    // ─────────────────────────────────────────────────────────────────────────

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_carousel);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  1. Material Carousel
    // ─────────────────────────────────────────────────────────────────────────

    private void setupCarousel() {
        carouselRecycler = findViewById(R.id.carousel_recycler);

        // Multi-browse horizontal layout with snap + peek
        LinearLayoutManager lm = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        carouselRecycler.setLayoutManager(lm);

        // Snap helper — snap each item to start
        androidx.recyclerview.widget.LinearSnapHelper snapHelper =
                new androidx.recyclerview.widget.LinearSnapHelper();
        snapHelper.attachToRecyclerView(carouselRecycler);

        // Build data
        String[] titles = getResources().getStringArray(R.array.carousel_titles);
        int[] bgColors = {
                Color.parseColor("#4A148C"),
                Color.parseColor("#880E4F"),
                Color.parseColor("#1A237E"),
                Color.parseColor("#1B5E20"),
                Color.parseColor("#E65100"),
                Color.parseColor("#37474F")
        };

        List<CarouselItem> items = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            items.add(new CarouselItem(titles[i], bgColors[i % bgColors.length]));
        }

        carouselAdapter = new CarouselAdapter(items);
        carouselRecycler.setAdapter(carouselAdapter);

        // Scale peek items via scroll listener (multi-browse effect)
        carouselRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                applyCarouselScaleTransform(rv);
            }
        });
        // Initial scale pass
        carouselRecycler.post(() -> applyCarouselScaleTransform(carouselRecycler));
    }

    /**
     * Applies scale transform to carousel items based on their distance from
     * the centre of the RecyclerView — the hero item is full size, peek items
     * are scaled down, creating the multi-browse carousel effect.
     */
    private void applyCarouselScaleTransform(RecyclerView rv) {
        int rvCenterX = rv.getWidth() / 2;
        for (int i = 0; i < rv.getChildCount(); i++) {
            View child = rv.getChildAt(i);
            float childCenterX = child.getLeft() + child.getWidth() / 2f;
            float distanceFromCenter = Math.abs(rvCenterX - childCenterX);
            float maxDistance = rvCenterX + child.getWidth();
            float ratio = 1f - Math.min(1f, distanceFromCenter / maxDistance);
            // scale from 0.82 (peek) to 1.0 (hero)
            float scale = 0.82f + (0.18f * ratio);
            child.setScaleX(scale);
            child.setScaleY(scale);
            child.setAlpha(0.6f + (0.4f * ratio));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  2. Stacked Cards — Z-axis wallet-style deck
    // ─────────────────────────────────────────────────────────────────────────

    private void setupStackedCards() {
        stackedCardsContainer = findViewById(R.id.stacked_cards_container);

        // Build card data
        String[] titles    = getResources().getStringArray(R.array.stacked_card_titles);
        String[] subtitles = getResources().getStringArray(R.array.stacked_card_subtitles);
        int[] progresses   = { 80, 65, 45, 90, 30 };

        cardColors = new int[]{
                getColor(R.color.card_color_1),
                getColor(R.color.card_color_2),
                getColor(R.color.card_color_3),
                getColor(R.color.card_color_4),
                getColor(R.color.card_color_5)
        };

        stackData.clear();
        for (int i = 0; i < CARD_COUNT; i++) {
            stackData.add(new StackedCardData(titles[i], subtitles[i], progresses[i]));
        }
        topCardIndex = 0;

        buildStackedCards();

        // Reset button
        MaterialButton btnReset = findViewById(R.id.btn_reset_stack);
        btnReset.setOnClickListener(v -> resetStack());
    }

    /**
     * Inflates all stack cards and positions them with offset/scale to create
     * the 3-D deck illusion. The top card gets a swipe-gesture detector.
     */
    private void buildStackedCards() {
        stackedCardsContainer.removeAllViews();
        float density = getResources().getDisplayMetrics().density;
        int count = Math.min(CARD_COUNT - topCardIndex, CARD_COUNT);

        // Add cards back-to-front so top card is last (highest z)
        for (int i = count - 1; i >= 0; i--) {
            int dataIndex = topCardIndex + i;
            if (dataIndex >= stackData.size()) continue;

            View cardView = LayoutInflater.from(this)
                    .inflate(R.layout.item_stacked_card, stackedCardsContainer, false);

            MaterialCardView card = (MaterialCardView) cardView;
            StackedCardData data = stackData.get(dataIndex);

            // Fill content
            ((TextView) card.findViewById(R.id.stacked_card_title))
                    .setText(data.title);
            ((TextView) card.findViewById(R.id.stacked_card_subtitle))
                    .setText(data.subtitle);
            LinearProgressIndicator prog =
                    card.findViewById(R.id.stacked_card_progress);
            prog.setProgressCompat(data.progress, false);

            // Color per card
            int color = cardColors[dataIndex % cardColors.length];
            card.setCardBackgroundColor(color);
            card.setStrokeWidth(0);
            ((TextView) card.findViewById(R.id.stacked_card_title))
                    .setTextColor(Color.WHITE);
            ((TextView) card.findViewById(R.id.stacked_card_subtitle))
                    .setTextColor(0xCCFFFFFF);
            prog.setIndicatorColor(Color.WHITE);
            prog.setTrackColor(0x40FFFFFF);

            // Position: offset + scale so each card peeks behind the one above
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (200 * density));
            float offsetY = i * CARD_OFFSET_DP * density;
            lp.topMargin = (int) offsetY;
            lp.leftMargin  = (int) (i * 4 * density);
            lp.rightMargin = (int) (i * 4 * density);
            card.setLayoutParams(lp);
            float scale = 1f - (i * CARD_SCALE_STEP);
            card.setScaleX(scale);
            card.setScaleY(scale);
            card.setElevation((count - i) * 4 * density);

            // Only attach swipe gesture to the TOP card (i == 0)
            if (i == 0) {
                attachSwipeGesture(card, dataIndex);
            }

            stackedCardsContainer.addView(cardView);
        }
    }

    /**
     * Attaches a horizontal fling/swipe gesture to a card.
     * Swiping left or right beyond a threshold dismisses the top card with
     * a fly-out animation, then reveals the card below.
     */
    private void attachSwipeGesture(final MaterialCardView card, final int dataIndex) {
        final float SWIPE_THRESHOLD = 200f;
        final float[] startX = {0f};
        final float[] dX = {0f};

        card.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX[0] = event.getRawX();
                    dX[0] = 0f;
                    card.animate().cancel();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    dX[0] = event.getRawX() - startX[0];
                    card.setTranslationX(dX[0]);
                    // Tilt the card as it's dragged
                    card.setRotation(dX[0] / 20f);
                    // Fade out as it moves
                    float alpha = 1f - Math.abs(dX[0]) / 600f;
                    card.setAlpha(Math.max(0.3f, alpha));
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (Math.abs(dX[0]) > SWIPE_THRESHOLD) {
                        // Dismiss: fly the card off screen
                        float direction = dX[0] > 0 ? 1f : -1f;
                        int screenWidth = getResources().getDisplayMetrics().widthPixels;
                        card.animate()
                                .translationX(direction * screenWidth * 1.5f)
                                .rotation(direction * 25f)
                                .alpha(0f)
                                .setDuration(300)
                                .setInterpolator(new AccelerateInterpolator(1.5f))
                                .withEndAction(() -> {
                                    topCardIndex++;
                                    if (topCardIndex >= stackData.size()) {
                                        topCardIndex = stackData.size();
                                        showSnackbar("All cards dismissed — tap Reset");
                                        stackedCardsContainer.removeAllViews();
                                    } else {
                                        buildStackedCards();
                                        animateNewTopCard();
                                    }
                                })
                                .start();
                        showSnackbar("Card " + (dataIndex + 1) + " dismissed");
                    } else {
                        // Snap back
                        card.animate()
                                .translationX(0f)
                                .rotation(0f)
                                .alpha(1f)
                                .setDuration(200)
                                .setInterpolator(new OvershootInterpolator(2f))
                                .start();
                    }
                    return true;
            }
            return false;
        });
    }

    /**
     * When a new card becomes the top, give it a spring-in entrance.
     */
    private void animateNewTopCard() {
        if (stackedCardsContainer.getChildCount() == 0) return;
        // The top card is the last child (highest index)
        View topCard = stackedCardsContainer.getChildAt(
                stackedCardsContainer.getChildCount() - 1);
        topCard.setScaleX(0.8f);
        topCard.setScaleY(0.8f);
        topCard.setAlpha(0.5f);
        topCard.animate()
                .scaleX(1f).scaleY(1f)
                .alpha(1f)
                .setDuration(350)
                .setInterpolator(new OvershootInterpolator(1.5f))
                .start();
    }

    private void resetStack() {
        topCardIndex = 0;
        buildStackedCards();
        // Entrance animation for the whole stack
        for (int i = 0; i < stackedCardsContainer.getChildCount(); i++) {
            View child = stackedCardsContainer.getChildAt(i);
            child.setAlpha(0f);
            child.setTranslationY(60f);
            child.animate()
                    .alpha(1f).translationY(0f)
                    .setStartDelay(i * 60L)
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator(2f))
                    .start();
        }
        showSnackbar("Stack reset — swipe the top card!");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  3. Skeleton / Shimmer Loader
    // ─────────────────────────────────────────────────────────────────────────

    private void setupShimmer() {
        shimmerContainer = findViewById(R.id.shimmer_container);

        MaterialButton btnToggle = findViewById(R.id.btn_toggle_shimmer);
        btnToggle.setOnClickListener(v -> {
            shimmerRunning = !shimmerRunning;
            if (shimmerRunning) {
                startShimmerAnimation();
                btnToggle.setText("Toggle Shimmer (ON)");
            } else {
                stopShimmerAnimation();
                btnToggle.setText("Toggle Shimmer (OFF)");
                // Reset all shimmer views to base color
                resetShimmerViews();
            }
        });

        startShimmerAnimation();
    }

    /**
     * Shimmer animation: ValueAnimator sweeps a highlight gradient across all
     * shimmer placeholder views using setAlpha cycling. A more advanced version
     * would use a custom Drawable with a moving LinearGradient — this approach
     * works universally without external libraries.
     */
    private void startShimmerAnimation() {
        stopShimmerAnimation(); // prevent double-start

        shimmerAnimator = ValueAnimator.ofFloat(0f, 1f);
        shimmerAnimator.setDuration(1200);
        shimmerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        shimmerAnimator.setRepeatMode(ValueAnimator.RESTART);

        final View[] shimmerViews = collectShimmerViews();

        shimmerAnimator.addUpdateListener(anim -> {
            float fraction = (float) anim.getAnimatedValue();
            // Stagger the highlight across views
            for (int i = 0; i < shimmerViews.length; i++) {
                float viewFraction = (fraction + i * 0.12f) % 1f;
                // Highlight pulse: bright in the middle of the sweep
                float pulse = (float) Math.sin(viewFraction * Math.PI);
                int baseAlpha = 0xE0;
                int highlightAlpha = 0xFF;
                int alpha = baseAlpha + (int) ((highlightAlpha - baseAlpha) * pulse);
                shimmerViews[i].setAlpha(alpha / 255f);

                // Translate highlight band across each view
                float translateX = (viewFraction - 0.5f) * shimmerViews[i].getWidth() * 2f;
                shimmerViews[i].setTranslationX(translateX * 0.08f);
            }
        });
        shimmerAnimator.start();
    }

    private void stopShimmerAnimation() {
        if (shimmerAnimator != null) {
            shimmerAnimator.cancel();
            shimmerAnimator = null;
        }
    }

    private void resetShimmerViews() {
        for (View v : collectShimmerViews()) {
            v.setAlpha(1f);
            v.setTranslationX(0f);
        }
    }

    private View[] collectShimmerViews() {
        return new View[]{
                shimmerContainer.findViewById(R.id.shimmer_avatar),
                shimmerContainer.findViewById(R.id.shimmer_line1),
                shimmerContainer.findViewById(R.id.shimmer_line2),
                shimmerContainer.findViewById(R.id.shimmer_block)
        };
    }

    private void showSnackbar(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Data Models
    // ─────────────────────────────────────────────────────────────────────────

    static class CarouselItem {
        final String title;
        final int color;
        CarouselItem(String title, int color) {
            this.title = title;
            this.color = color;
        }
    }

    static class StackedCardData {
        final String title;
        final String subtitle;
        final int progress;
        StackedCardData(String title, String subtitle, int progress) {
            this.title    = title;
            this.subtitle = subtitle;
            this.progress = progress;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Carousel RecyclerView Adapter
    // ─────────────────────────────────────────────────────────────────────────

    static class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.VH> {

        private final List<CarouselItem> items;

        CarouselAdapter(List<CarouselItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_carousel, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            CarouselItem item = items.get(position);
            holder.title.setText(item.title);
            holder.image.setBackgroundColor(item.color);
            holder.card.setOnClickListener(v ->
                    Snackbar.make(v, "Carousel: " + item.title, Snackbar.LENGTH_SHORT).show());
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            MaterialCardView card;
            ImageView image;
            TextView title;
            VH(@NonNull View itemView) {
                super(itemView);
                card  = (MaterialCardView) itemView;
                image = itemView.findViewById(R.id.carousel_image);
                title = itemView.findViewById(R.id.carousel_title);
            }
        }
    }
}
