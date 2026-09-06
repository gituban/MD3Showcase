package com.md3showcase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * AdvancedActivity
 *
 * Demonstrates the "bespoke tier" of Material Design 3 advanced UI patterns:
 *
 * 1. Circular Reveal — ViewAnimationUtils.createCircularReveal()
 *    A reveal surface expands outward from the button's tap coordinate.
 *
 * 2. Container Transform (Morph) — FAB → expanded panel
 *    The FAB scale-animates while the expanded content fades+scales in,
 *    simulating the M3 container transform shared transition.
 *
 * 3. Spring Physics Animation — SpringAnimation (stiffness + damping)
 *    A ball card bounces across the screen with realistic physics.
 *
 * 4. Draggable Panel — custom OnTouchListener with raw coordinate tracking
 *    Two draggable views: one inside a bounded FrameLayout, one free-floating
 *    overlay that docks to the nearest screen edge on release.
 *
 * 5. Glassmorphism — RenderEffect.createBlurEffect() on API 31+
 *    Falls back to a semi-transparent frosted overlay on older devices.
 *
 * 6. Sticky Headers — RecyclerView with a custom ItemDecoration that
 *    draws the section header at y=0 while its section is visible.
 */
public class AdvancedActivity extends AppCompatActivity {

    private View rootView;

    // ── Circular Reveal ──
    private View revealSurface;
    private TextView revealText;
    private MaterialButton btnTriggerReveal;
    private boolean revealVisible = false;

    // ── Morph ──
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabMorphSource;
    private LinearLayout morphExpanded;

    // ── Spring ──
    private MaterialCardView springBall;
    private SpringAnimation springAnimX;

    // ── Draggable overlay ──
    private MaterialCardView floatingDraggable;

    // ── Blur toggle ──
    private MaterialCardView glassCard;
    private boolean blurEnabled = false;

    // ─────────────────────────────────────────────────────────────────────────
    //  Lifecycle
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        rootView = findViewById(android.R.id.content);

        setupToolbar();
        setupCircularReveal();
        setupContainerMorph();
        setupSpringPhysics();
        setupDraggablePanel();
        setupGlassmorphism();
        setupStickyHeaders();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Toolbar
    // ─────────────────────────────────────────────────────────────────────────

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_advanced);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  1. Circular Reveal
    // ─────────────────────────────────────────────────────────────────────────

    private void setupCircularReveal() {
        revealSurface   = findViewById(R.id.reveal_surface);
        revealText      = findViewById(R.id.reveal_text);
        btnTriggerReveal = findViewById(R.id.btn_trigger_reveal);

        btnTriggerReveal.setOnClickListener(v -> {
            if (!revealVisible) {
                performReveal(v);
            } else {
                performHide(v);
            }
        });
    }

    private void performReveal(View anchorView) {
        revealSurface.setVisibility(View.VISIBLE);
        revealText.setVisibility(View.VISIBLE);

        // Center of the anchor button as reveal epicentre
        int cx = revealSurface.getWidth() / 2;
        int cy = revealSurface.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy) * 1.1f;

        Animator anim = ViewAnimationUtils.createCircularReveal(
                revealSurface, cx, cy, 0f, finalRadius);
        anim.setDuration(500);
        anim.setInterpolator(new android.view.animation.DecelerateInterpolator(2f));
        anim.start();

        revealText.setAlpha(0f);
        revealText.animate().alpha(1f).setStartDelay(250).setDuration(200).start();

        btnTriggerReveal.setText("Hide Reveal");
        revealVisible = true;
    }

    private void performHide(View anchorView) {
        int cx = revealSurface.getWidth() / 2;
        int cy = revealSurface.getHeight() / 2;
        float initRadius = (float) Math.hypot(cx, cy) * 1.1f;

        Animator anim = ViewAnimationUtils.createCircularReveal(
                revealSurface, cx, cy, initRadius, 0f);
        anim.setDuration(400);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                revealSurface.setVisibility(View.INVISIBLE);
                revealText.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
        revealText.animate().alpha(0f).setDuration(150).start();
        btnTriggerReveal.setText("Tap to Reveal");
        revealVisible = false;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  2. Container Morph (FAB → Expanded Panel)
    // ─────────────────────────────────────────────────────────────────────────

    private void setupContainerMorph() {
        fabMorphSource = findViewById(R.id.fab_morph_source);
        morphExpanded  = findViewById(R.id.morph_expanded);
        MaterialButton btnClose = morphExpanded.findViewById(R.id.btn_morph_close);

        fabMorphSource.setOnClickListener(v -> animateMorphExpand());
        btnClose.setOnClickListener(v -> animateMorphCollapse());
    }

    private void animateMorphExpand() {
        // Scale the FAB down while the expanded content grows in
        fabMorphSource.animate()
                .scaleX(0f).scaleY(0f).alpha(0f)
                .setDuration(200)
                .setInterpolator(new android.view.animation.AccelerateInterpolator())
                .withEndAction(() -> {
                    fabMorphSource.setVisibility(View.GONE);
                    morphExpanded.setVisibility(View.VISIBLE);
                    morphExpanded.setScaleX(0.3f);
                    morphExpanded.setScaleY(0.3f);
                    morphExpanded.setAlpha(0f);
                    morphExpanded.animate()
                            .scaleX(1f).scaleY(1f).alpha(1f)
                            .setDuration(350)
                            .setInterpolator(new android.view.animation.OvershootInterpolator(1.2f))
                            .start();
                })
                .start();
    }

    private void animateMorphCollapse() {
        morphExpanded.animate()
                .scaleX(0.3f).scaleY(0.3f).alpha(0f)
                .setDuration(220)
                .setInterpolator(new android.view.animation.AccelerateInterpolator())
                .withEndAction(() -> {
                    morphExpanded.setVisibility(View.GONE);
                    fabMorphSource.setVisibility(View.VISIBLE);
                    fabMorphSource.setScaleX(0f);
                    fabMorphSource.setScaleY(0f);
                    fabMorphSource.setAlpha(0f);
                    fabMorphSource.animate()
                            .scaleX(1f).scaleY(1f).alpha(1f)
                            .setDuration(350)
                            .setInterpolator(new android.view.animation.OvershootInterpolator(2f))
                            .start();
                })
                .start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  3. Spring Physics Animation
    // ─────────────────────────────────────────────────────────────────────────

    private void setupSpringPhysics() {
        springBall = findViewById(R.id.spring_ball);
        MaterialButton btnSpring = findViewById(R.id.btn_spring_animate);

        // X-axis spring: fling the ball to the right then spring back
        springAnimX = new SpringAnimation(springBall, DynamicAnimation.TRANSLATION_X)
                .setSpring(new SpringForce(0f)
                        .setStiffness(SpringForce.STIFFNESS_MEDIUM)
                        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY));

        // Y-axis spring for a secondary bounce effect
        SpringAnimation springAnimY = new SpringAnimation(springBall, DynamicAnimation.TRANSLATION_Y)
                .setSpring(new SpringForce(0f)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                        .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY));

        btnSpring.setOnClickListener(v -> {
            // Give the ball a velocity flick, let spring bring it back
            springAnimX.cancel();
            springAnimY.cancel();

            float flingVelocity = 2500f;
            springAnimX.setStartVelocity(flingVelocity);
            springAnimY.setStartVelocity(-800f);
            springAnimX.start();
            springAnimY.start();

            showSnackbar("Spring! stiffness=MEDIUM, damping=BOUNCY");
        });

        // Also make the ball tappable for a secondary flick
        springBall.setOnClickListener(v -> {
            springAnimX.cancel();
            springAnimX.setStartValue(springBall.getTranslationX());
            springAnimX.setStartVelocity(-1500f);
            springAnimX.start();
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  4. Draggable Panels
    // ─────────────────────────────────────────────────────────────────────────

    private void setupDraggablePanel() {
        // ── Bounded draggable (inside FrameLayout) ──
        MaterialCardView boundedPanel = findViewById(R.id.draggable_panel);
        attachBoundedDrag(boundedPanel);

        // ── Free-floating overlay (docks to nearest edge on release) ──
        floatingDraggable = findViewById(R.id.floating_draggable);
        attachFreeFloatingDrag(floatingDraggable);
    }

    private void attachBoundedDrag(View panel) {
        final float[] dX = {0f}, dY = {0f};
        panel.setOnTouchListener((v, event) -> {
            ViewGroup parent = (ViewGroup) v.getParent();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX[0] = v.getX() - event.getRawX();
                    dY[0] = v.getY() - event.getRawY();
                    v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).start();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float newX = event.getRawX() + dX[0];
                    float newY = event.getRawY() + dY[0];
                    // Clamp within parent bounds
                    newX = Math.max(0, Math.min(newX, parent.getWidth()  - v.getWidth()));
                    newY = Math.max(0, Math.min(newY, parent.getHeight() - v.getHeight()));
                    v.setX(newX);
                    v.setY(newY);
                    return true;

                case MotionEvent.ACTION_UP:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
                    showSnackbar("Panel at (" + (int) v.getX() + ", " + (int) v.getY() + ")");
                    return true;
            }
            return false;
        });
    }

    /**
     * Free-floating drag that snaps to the nearest screen edge (left or right)
     * with a spring animation on release.
     */
    private void attachFreeFloatingDrag(final View panel) {
        final float[] dX = {0f}, dY = {0f};

        panel.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX[0] = v.getX() - event.getRawX();
                    dY[0] = v.getY() - event.getRawY();
                    v.animate().scaleX(1.1f).scaleY(1.1f)
                            .setDuration(100).start();
                    v.setElevation(24f * getResources().getDisplayMetrics().density);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float newX = event.getRawX() + dX[0];
                    float newY = event.getRawY() + dY[0];
                    // Clamp vertically to screen
                    int screenH = getResources().getDisplayMetrics().heightPixels;
                    newY = Math.max(0, Math.min(newY, screenH - v.getHeight()));
                    v.setX(newX);
                    v.setY(newY);
                    return true;

                case MotionEvent.ACTION_UP:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
                    v.setElevation(16f * getResources().getDisplayMetrics().density);
                    snapToEdge(v);
                    return true;
            }
            return false;
        });
    }

    /**
     * After the user releases the floating panel, spring it to whichever
     * horizontal edge (left or right) it is closer to.
     */
    private void snapToEdge(View panel) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        float panelCenterX = panel.getX() + panel.getWidth() / 2f;
        float margin = 16 * getResources().getDisplayMetrics().density;

        float targetX;
        if (panelCenterX < screenWidth / 2f) {
            targetX = margin; // snap left
        } else {
            targetX = screenWidth - panel.getWidth() - margin; // snap right
        }

        SpringAnimation snapAnim = new SpringAnimation(panel, DynamicAnimation.X, targetX)
                .setSpring(new SpringForce(targetX)
                        .setStiffness(SpringForce.STIFFNESS_MEDIUM)
                        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY));
        snapAnim.start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  5. Glassmorphism (RenderEffect on API 31+)
    // ─────────────────────────────────────────────────────────────────────────

    private void setupGlassmorphism() {
        glassCard = findViewById(R.id.glass_card);
        MaterialButton btnBlur = findViewById(R.id.btn_toggle_blur);

        // Apply blur immediately if API 31+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applyBlur(glassCard, 18f);
            blurEnabled = true;
            btnBlur.setText("Disable Blur");
        } else {
            btnBlur.setText("Blur (API 31+ only)");
        }

        btnBlur.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (blurEnabled) {
                    glassCard.setRenderEffect(null);
                    blurEnabled = false;
                    btnBlur.setText("Enable Blur");
                    showSnackbar("Blur disabled");
                } else {
                    applyBlur(glassCard, 18f);
                    blurEnabled = true;
                    btnBlur.setText("Disable Blur");
                    showSnackbar("RenderEffect blur enabled (API 31+)");
                }
            } else {
                showSnackbar("RenderEffect requires Android 12 (API 31+)");
            }
        });
    }

    private void applyBlur(View target, float radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            target.setRenderEffect(
                    RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  6. Sticky Headers RecyclerView
    // ─────────────────────────────────────────────────────────────────────────

    private void setupStickyHeaders() {
        RecyclerView stickyRecycler = findViewById(R.id.sticky_recycler);
        stickyRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Build a flat list that includes header items and row items
        List<ListItem> items = buildStickyData();
        StickyHeaderAdapter adapter = new StickyHeaderAdapter(items);
        stickyRecycler.setAdapter(adapter);

        // Attach the custom sticky-header ItemDecoration
        stickyRecycler.addItemDecoration(new StickyHeaderDecoration(adapter));
    }

    private List<ListItem> buildStickyData() {
        List<ListItem> list = new ArrayList<>();
        String[][] groups = {
                {"Navigation Components",
                        "Bottom Navigation Bar", "Navigation Rail",
                        "Navigation Drawer", "Top App Bar"},
                {"Input Components",
                        "Text Fields", "Checkboxes",
                        "Radio Buttons", "Sliders"},
                {"Feedback Components",
                        "Snackbars", "Dialogs",
                        "Progress Indicators", "Badges"}
        };
        for (String[] group : groups) {
            list.add(new ListItem(group[0], null, true)); // header
            for (int i = 1; i < group.length; i++) {
                list.add(new ListItem(group[i], "Component · Material Design 3", false));
            }
        }
        return list;
    }

    private void showSnackbar(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Data + Adapter for Sticky Headers
    // ─────────────────────────────────────────────────────────────────────────

    static class ListItem {
        final String title;
        final String subtitle;
        final boolean isHeader;
        ListItem(String title, String subtitle, boolean isHeader) {
            this.title    = title;
            this.subtitle = subtitle;
            this.isHeader = isHeader;
        }
    }

    static class StickyHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM   = 1;
        private final List<ListItem> items;

        StickyHeaderAdapter(List<ListItem> items) { this.items = items; }

        @Override public int getItemViewType(int position) {
            return items.get(position).isHeader ? TYPE_HEADER : TYPE_ITEM;
        }

        @NonNull @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            if (viewType == TYPE_HEADER) {
                View v = inf.inflate(R.layout.item_sticky_header, parent, false);
                return new HeaderVH(v);
            } else {
                View v = inf.inflate(R.layout.item_list_row, parent, false);
                return new RowVH(v);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ListItem item = items.get(position);
            if (holder instanceof HeaderVH) {
                ((HeaderVH) holder).title.setText(item.title);
            } else {
                RowVH row = (RowVH) holder;
                row.title.setText(item.title);
                row.subtitle.setText(item.subtitle);
                row.icon.setImageResource(R.drawable.ic_check);
            }
        }

        @Override public int getItemCount() { return items.size(); }

        /** Returns the position of the header for the section containing [position]. */
        int getHeaderPositionForItem(int itemPosition) {
            for (int i = itemPosition; i >= 0; i--) {
                if (items.get(i).isHeader) return i;
            }
            return 0;
        }

        String getHeaderTitle(int headerPosition) {
            return items.get(headerPosition).title;
        }

        static class HeaderVH extends RecyclerView.ViewHolder {
            TextView title;
            HeaderVH(@NonNull View v) {
                super(v);
                title = v.findViewById(R.id.header_title);
            }
        }

        static class RowVH extends RecyclerView.ViewHolder {
            TextView title, subtitle;
            android.widget.ImageView icon;
            RowVH(@NonNull View v) {
                super(v);
                title    = v.findViewById(R.id.item_title);
                subtitle = v.findViewById(R.id.item_subtitle);
                icon     = v.findViewById(R.id.item_icon);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Sticky Header ItemDecoration
    //
    //  Draws the current section's header at y=0 (top of the RecyclerView),
    //  pushing it up as the next header approaches.
    // ─────────────────────────────────────────────────────────────────────────

    static class StickyHeaderDecoration extends RecyclerView.ItemDecoration {

        private final StickyHeaderAdapter adapter;

        StickyHeaderDecoration(StickyHeaderAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onDrawOver(@NonNull android.graphics.Canvas canvas,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
            super.onDrawOver(canvas, parent, state);

            View topChild = parent.getChildAt(0);
            if (topChild == null) return;

            int topChildPosition = parent.getChildAdapterPosition(topChild);
            if (topChildPosition == RecyclerView.NO_ID) return;

            int headerPos = adapter.getHeaderPositionForItem(topChildPosition);
            View headerView = getHeaderViewForPos(parent, headerPos);
            if (headerView == null) return;

            // Check if the next header is about to push this one up
            int contactPoint = headerView.getBottom();
            View childInContact = getChildInContact(parent, contactPoint, headerPos);
            int translateY = 0;
            if (childInContact != null) {
                int contactChildPos = parent.getChildAdapterPosition(childInContact);
                if (contactChildPos != RecyclerView.NO_ID
                        && adapter.items.get(contactChildPos).isHeader) {
                    translateY = Math.min(0, childInContact.getTop() - headerView.getHeight());
                }
            }

            canvas.save();
            canvas.translate(0, translateY);
            headerView.draw(canvas);
            canvas.restore();
        }

        private View getHeaderViewForPos(RecyclerView parent, int headerPosition) {
            RecyclerView.ViewHolder holder =
                    adapter.createViewHolder(parent, StickyHeaderAdapter.TYPE_HEADER);
            adapter.onBindViewHolder(holder, headerPosition);
            View view = holder.itemView;
            int widthSpec  = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                    View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                    View.MeasureSpec.UNSPECIFIED);
            int childWidth  = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);
            view.measure(childWidth, childHeight);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            return view;
        }

        private View getChildInContact(RecyclerView parent, int contactPoint, int currentHeaderPos) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.getBottom() > contactPoint && child.getTop() <= contactPoint) {
                    int childAdapterPos = parent.getChildAdapterPosition(child);
                    if (childAdapterPos != RecyclerView.NO_ID
                            && childAdapterPos != currentHeaderPos) {
                        return child;
                    }
                }
            }
            return null;
        }
    }
}
