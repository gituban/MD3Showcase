package com.md3showcase;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.snackbar.Snackbar;

/**
 * ListPatternsActivity
 *
 * Demonstrates the most common RecyclerView / Card list patterns in Material apps:
 *
 * 1. ONE-LINE LIST  — simple label rows separated by dividers
 * 2. TWO-LINE LIST  — title + subtitle rows
 * 3. THREE-LINE LIST — title + subtitle + body preview (email-style)
 * 4. VERTICAL CARD LIST — icon-circle + text (settings/library style)
 * 5. HORIZONTAL CATEGORY ROW — HorizontalScrollView card strip
 * 6. 2-COLUMN GRID — RecyclerView + GridLayoutManager(2)
 *
 * Each section includes:
 *   • Live scrollable demo
 *   • XML snippet dialog showing the row layout and RecyclerView setup
 */
public class ListPatternsActivity extends AppCompatActivity {

    private View rootView;
    private float dp;

    // ── Data ──────────────────────────────────────────────────────────────────

    private static final String[][] TWO_LINE = {
        {"Ali Connors",    "Brunch this weekend?"},
        {"Meg Hansson",    "RE: Conference next week"},
        {"Trevor Hansen",  "Dog photos"},
        {"Sandra Adams",   "Trip report — see attachment"},
        {"Britta Holt",    "Design review on Tuesday?"},
    };

    private static final String[][] THREE_LINE = {
        {"Sandra Adams",  "Trip report",    "Hi, I'll be in the area on Tuesday and wanted to grab coffee if you're free…"},
        {"Britta Holt",   "Design review",  "Can we schedule a call this week to go over the Figma file? I have some questions…"},
        {"Ali Connors",   "Weekend plans",  "Are you and the family coming to the lakehouse? We have space for six and the weather should be perfect…"},
    };

    private static final String[] SETTINGS_TITLES = {
        "Appearance", "Playback", "Behavior", "AI Integration",
        "Backup & Restore", "Developer Options", "Equalizer", "About"
    };
    private static final String[] SETTINGS_SUBS = {
        "Themes, layout, and visual styles",
        "Audio behavior, crossfade, and background",
        "Gestures, haptics, and navigation",
        "AI providers, API keys, and model settings",
        "Export and recover your personal data",
        "Experimental features and debugging",
        "Adjust audio frequencies and presets",
        "App info, version, and credits"
    };
    private static final int[] SETTINGS_COLORS = {
        Color.parseColor("#634551"), Color.parseColor("#5A3945"),
        Color.parseColor("#405568"), Color.parseColor("#004F5E"),
        Color.parseColor("#3B466A"), Color.parseColor("#2C5036"),
        Color.parseColor("#694E1A"), Color.parseColor("#00525C")
    };

    private static final String[] CATEGORY_LABELS = {"Afrobeats","Bongo","Gospel","Hip-Hop","R&B","Dancehall"};
    private static final int[] CATEGORY_COLORS = {
        Color.parseColor("#6B3A2A"), Color.parseColor("#2A4A6B"),
        Color.parseColor("#2A6B3A"), Color.parseColor("#5C2A6B"),
        Color.parseColor("#6B5A2A"), Color.parseColor("#2A6B5A")
    };

    private static final String[] GRID_TITLES = {"Chill Vibes","Late Night","Morning Run","Focus Mode","Party Mix","Acoustic"};
    private static final String[] GRID_SUBS   = {"32 tracks","18 tracks","45 tracks","27 tracks","60 tracks","22 tracks"};
    private static final int[] GRID_COLORS = {
        Color.parseColor("#3B5E8C"), Color.parseColor("#7B3F5E"),
        Color.parseColor("#2E6B3A"), Color.parseColor("#5E4A2A"),
        Color.parseColor("#8C3D3D"), Color.parseColor("#3D5E5E")
    };

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dp = getResources().getDisplayMetrics().density;

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView = root;

        MaterialToolbar toolbar = new MaterialToolbar(this);
        toolbar.setTitle("List & Card Patterns");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, px(56)));
        root.addView(toolbar);

        NestedScrollView scroll = new NestedScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        scroll.setClipToPadding(false);
        scroll.setPadding(px(16), px(16), px(16), px(88));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        // 1. One-line list
        content.addView(label("ONE-LINE LIST"));
        content.addView(caption("Simple label rows with MaterialDividers — common for settings or menu items."));
        content.addView(buildOneLineList());
        content.addView(snippetBtn("One-line List Row",
            "<!-- Row layout (item_one_line.xml) -->\n" +
            "<LinearLayout\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"56dp\"\n" +
            "    android:gravity=\"center_vertical\"\n" +
            "    android:paddingStart=\"16dp\"\n" +
            "    android:paddingEnd=\"16dp\">\n\n" +
            "    <TextView\n" +
            "        android:id=\"@+id/title\"\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:textAppearance=\"?attr/textAppearanceBodyLarge\" />\n" +
            "</LinearLayout>\n\n" +
            "// RecyclerView:\n" +
            "recycler.layoutManager = LinearLayoutManager(context)\n" +
            "recycler.adapter = MyAdapter(items)"
        ));

        // 2. Two-line list
        content.addView(label("TWO-LINE LIST"));
        content.addView(caption("Title + subtitle rows — email inbox, contacts, music library."));
        content.addView(buildTwoLineList());

        // 3. Three-line list
        content.addView(label("THREE-LINE LIST"));
        content.addView(caption("Title + subhead + body preview — email detail, message threads."));
        content.addView(buildThreeLineList());

        // 4. Settings-style list (icon circle + text)
        content.addView(label("SETTINGS LIST — ICON CIRCLES"));
        content.addView(caption("Colored icon circle + title/subtitle. Position-aware corner rounding."));
        content.addView(buildSettingsList());
        content.addView(snippetBtn("Settings List Row",
            "// Row programmatic pattern (Kotlin):\n" +
            "LinearLayout(context).apply {\n" +
            "    orientation = HORIZONTAL\n" +
            "    gravity = Gravity.CENTER_VERTICAL\n" +
            "    minimumHeight = 72.dp\n" +
            "    setPadding(16.dp, 0, 16.dp, 0)\n\n" +
            "    // Circle icon:\n" +
            "    MaterialCardView(context).apply {\n" +
            "        radius = 24.dp\n" +
            "        setCardBackgroundColor(iconColor)\n" +
            "        addView(ImageView(context))\n" +
            "    }\n\n" +
            "    // Text block:\n" +
            "    LinearLayout(context).apply {\n" +
            "        orientation = VERTICAL\n" +
            "        addView(TextView /* title */)\n" +
            "        addView(TextView /* subtitle */)\n" +
            "    }\n" +
            "}\n\n" +
            "// Position-aware corner radii (first/last/middle):\n" +
            "val outerR = 28.dp; val innerR = 4.dp\n" +
            "cornerRadii = when (index) {\n" +
            "    0            -> floatArrayOf(outerR,outerR,outerR,outerR,innerR,innerR,innerR,innerR)\n" +
            "    lastIndex    -> floatArrayOf(innerR,innerR,innerR,innerR,outerR,outerR,outerR,outerR)\n" +
            "    else         -> FloatArray(8) { innerR }\n" +
            "}"
        ));

        // 5. Horizontal category row
        content.addView(label("HORIZONTAL CARD ROW"));
        content.addView(caption("Sideways-scrolling cards for genre/category browsing or quick-access features."));
        content.addView(buildHorizontalRow());
        content.addView(snippetBtn("Horizontal Category Row",
            "// RecyclerView — horizontal:\n" +
            "recyclerView.layoutManager = LinearLayoutManager(\n" +
            "    context, LinearLayoutManager.HORIZONTAL, false)\n" +
            "recyclerView.clipToPadding = false\n" +
            "recyclerView.setPadding(16.dp, 0, 16.dp, 0)\n\n" +
            "// Or HorizontalScrollView + LinearLayout (for short, static lists):\n" +
            "<HorizontalScrollView\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:clipToPadding=\"false\"\n" +
            "    android:paddingStart=\"16dp\">\n" +
            "    <LinearLayout\n" +
            "        android:orientation=\"horizontal\" />\n" +
            "</HorizontalScrollView>"
        ));

        // 6. 2-column grid
        content.addView(label("2-COLUMN GRID"));
        content.addView(caption("GridLayoutManager(spanCount=2) — album art, playlists, component catalog style."));
        content.addView(buildGrid());
        content.addView(snippetBtn("Grid RecyclerView",
            "// 2-column grid:\n" +
            "recyclerView.layoutManager = GridLayoutManager(context, 2)\n" +
            "recyclerView.isNestedScrollingEnabled = false\n\n" +
            "// Variable span (full-width headers):\n" +
            "val glm = GridLayoutManager(context, 2)\n" +
            "glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {\n" +
            "    override fun getSpanSize(position: Int): Int =\n" +
            "        if (adapter.getItemViewType(position) == TYPE_HEADER) 2 else 1\n" +
            "}"
        ));

        scroll.addView(content);
        root.addView(scroll);
        setContentView(root);
    }

    // ── List builders ─────────────────────────────────────────────────────────

    private View buildOneLineList() {
        MaterialCardView card = demoCard();
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        String[] items = {"Inbox","Sent","Drafts","Spam","Trash"};
        for (int i = 0; i < items.length; i++) {
            inner.addView(oneLineRow(items[i]));
            if (i < items.length - 1) inner.addView(divider(px(0)));
        }
        card.addView(inner);
        return card;
    }

    private View buildTwoLineList() {
        MaterialCardView card = demoCard();
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < TWO_LINE.length; i++) {
            inner.addView(twoLineRow(TWO_LINE[i][0], TWO_LINE[i][1]));
            if (i < TWO_LINE.length - 1) inner.addView(divider(px(0)));
        }
        card.addView(inner);
        return card;
    }

    private View buildThreeLineList() {
        MaterialCardView card = demoCard();
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < THREE_LINE.length; i++) {
            inner.addView(threeLineRow(THREE_LINE[i][0], THREE_LINE[i][1], THREE_LINE[i][2]));
            if (i < THREE_LINE.length - 1) inner.addView(divider(px(0)));
        }
        card.addView(inner);
        return card;
    }

    private View buildSettingsList() {
        // Outer wrapper card with rounded top/bottom
        LinearLayout wrapper = new LinearLayout(this);
        wrapper.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams wlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.bottomMargin = px(8);
        wrapper.setLayoutParams(wlp);

        for (int i = 0; i < SETTINGS_TITLES.length; i++) {
            wrapper.addView(settingsRow(i, SETTINGS_TITLES.length,
                    SETTINGS_TITLES[i], SETTINGS_SUBS[i], SETTINGS_COLORS[i]));
        }
        return wrapper;
    }

    private View buildHorizontalRow() {
        android.widget.HorizontalScrollView hScroll = new android.widget.HorizontalScrollView(this);
        LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, px(96));
        hlp.bottomMargin = px(8);
        hScroll.setLayoutParams(hlp);
        hScroll.setClipToPadding(false);
        hScroll.setPadding(0, 0, px(16), 0);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 0, 0, 0);

        for (int i = 0; i < CATEGORY_LABELS.length; i++) {
            MaterialCardView card = new MaterialCardView(this);
            card.setRadius(dp * 14);
            card.setCardElevation(dp * 2);
            card.setCardBackgroundColor(CATEGORY_COLORS[i]);
            LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(px(110), px(80));
            clp.leftMargin = px(16);
            card.setLayoutParams(clp);

            TextView tv = new TextView(this);
            tv.setText(CATEGORY_LABELS[i]);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(13f);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            card.addView(tv);

            String label = CATEGORY_LABELS[i];
            card.setOnClickListener(v -> Toast.makeText(this, label, Toast.LENGTH_SHORT).show());
            row.addView(card);
        }
        hScroll.addView(row);
        return hScroll;
    }

    private View buildGrid() {
        RecyclerView rv = new RecyclerView(this);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)Math.ceil(GRID_TITLES.length / 2.0) * px(148));
        rlp.bottomMargin = px(8);
        rv.setLayoutParams(rlp);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override public int getItemCount() { return GRID_TITLES.length; }
            @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p, int t) {
                MaterialCardView card = new MaterialCardView(p.getContext());
                card.setRadius(dp * 16);
                card.setCardElevation(dp * 2);
                GridLayoutManager.LayoutParams glp = new GridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, px(140));
                glp.setMargins(px(4), px(4), px(4), px(4));
                card.setLayoutParams(glp);
                LinearLayout inner = new LinearLayout(p.getContext());
                inner.setOrientation(LinearLayout.VERTICAL);
                inner.setGravity(Gravity.BOTTOM);
                inner.setPadding(px(12), px(12), px(12), px(12));
                inner.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                TextView title = new TextView(p.getContext());
                title.setTextColor(Color.WHITE);
                title.setTextSize(14f);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setTag("title");
                TextView sub = new TextView(p.getContext());
                sub.setTextColor(Color.argb(180, 255, 255, 255));
                sub.setTextSize(11f);
                sub.setTag("sub");
                inner.addView(title);
                inner.addView(sub);
                card.addView(inner);
                return new RecyclerView.ViewHolder(card) {};
            }
            @Override public void onBindViewHolder(RecyclerView.ViewHolder h, int pos) {
                MaterialCardView card = (MaterialCardView) h.itemView;
                card.setCardBackgroundColor(GRID_COLORS[pos]);
                LinearLayout inner = (LinearLayout) card.getChildAt(0);
                ((TextView) inner.findViewWithTag("title")).setText(GRID_TITLES[pos]);
                ((TextView) inner.findViewWithTag("sub")).setText(GRID_SUBS[pos]);
                String t = GRID_TITLES[pos];
                card.setOnClickListener(v -> Toast.makeText(ListPatternsActivity.this, t, Toast.LENGTH_SHORT).show());
            }
        });
        return rv;
    }

    // ── Row builders ──────────────────────────────────────────────────────────

    private View oneLineRow(String text) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(px(16), px(14), px(16), px(14));
        row.setMinimumHeight(px(48));
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16f);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv);
        return row;
    }

    private View twoLineRow(String title, String sub) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(px(16), px(10), px(16), px(10));
        TextView t = new TextView(this);
        t.setText(title);
        t.setTextSize(16f);
        TextView s = new TextView(this);
        s.setText(sub);
        s.setTextSize(14f);
        s.setAlpha(0.6f);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        slp.topMargin = px(2);
        s.setLayoutParams(slp);
        row.addView(t);
        row.addView(s);
        return row;
    }

    private View threeLineRow(String title, String sub, String body) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(px(16), px(10), px(16), px(10));
        TextView t = new TextView(this);  t.setText(title); t.setTextSize(16f);
        TextView s = new TextView(this);  s.setText(sub);   s.setTextSize(14f);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.topMargin = px(2); s.setLayoutParams(lp1);
        TextView b = new TextView(this);  b.setText(body); b.setTextSize(12f);
        b.setAlpha(0.55f); b.setMaxLines(2);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.topMargin = px(2); b.setLayoutParams(lp2);
        row.addView(t); row.addView(s); row.addView(b);
        return row;
    }

    private View settingsRow(int index, int total, String title, String sub, int iconColor) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(px(16), px(14), px(16), px(14));
        row.setMinimumHeight(px(72));
        row.setClickable(true);
        row.setFocusable(true);

        // Position-aware corners
        float outerR = dp * 24;
        float innerR = dp * 4;
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#1A1A1A"));
        if (index == 0)
            bg.setCornerRadii(new float[]{outerR,outerR,outerR,outerR,innerR,innerR,innerR,innerR});
        else if (index == total - 1)
            bg.setCornerRadii(new float[]{innerR,innerR,innerR,innerR,outerR,outerR,outerR,outerR});
        else
            bg.setCornerRadii(new float[]{innerR,innerR,innerR,innerR,innerR,innerR,innerR,innerR});
        row.setBackground(bg);

        LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowLp.bottomMargin = px(3);
        row.setLayoutParams(rowLp);

        // Icon circle
        MaterialCardView iconCircle = new MaterialCardView(this);
        iconCircle.setRadius(dp * 20);
        iconCircle.setCardBackgroundColor(iconColor);
        iconCircle.setCardElevation(0f);
        iconCircle.setLayoutParams(new LinearLayout.LayoutParams(px(44), px(44)));
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_settings);
        iv.setColorFilter(Color.WHITE);
        iv.setPadding(px(10), px(10), px(10), px(10));
        iv.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iconCircle.addView(iv);
        row.addView(iconCircle);

        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams tcLp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tcLp.leftMargin = px(14);
        textCol.setLayoutParams(tcLp);
        TextView tv = new TextView(this);
        tv.setText(title);
        tv.setTextColor(Color.parseColor("#E4E3DA"));
        tv.setTextSize(16f);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        TextView sv = new TextView(this);
        sv.setText(sub);
        sv.setTextColor(Color.parseColor("#909285"));
        sv.setTextSize(13f);
        sv.setMaxLines(1);
        textCol.addView(tv);
        textCol.addView(sv);
        row.addView(textCol);

        String titleCopy = title;
        row.setOnClickListener(v -> Toast.makeText(this, titleCopy, Toast.LENGTH_SHORT).show());
        return row;
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    private MaterialButton snippetBtn(String title, String code) {
        MaterialButton btn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_TextButton);
        btn.setText("View XML Snippet");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = px(4);
        lp.bottomMargin = px(8);
        btn.setLayoutParams(lp);
        btn.setOnClickListener(v -> {
            ScrollView sv = new ScrollView(this);
            TextView tv = new TextView(this);
            int p = px(16);
            tv.setPadding(p, p, p, p);
            tv.setText(code);
            tv.setTextSize(12f);
            tv.setTypeface(android.graphics.Typeface.MONOSPACE);
            sv.addView(tv);
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                    .setTitle("📋  " + title)
                    .setView(sv)
                    .setPositiveButton("Close", null)
                    .setNeutralButton("Copy", (d, w) -> {
                        android.content.ClipboardManager cm =
                                (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cm.setPrimaryClip(android.content.ClipData.newPlainText("snip", code));
                        Snackbar.make(rootView, "Copied!", Snackbar.LENGTH_SHORT).show();
                    })
                    .show();
        });
        return btn;
    }

    private TextView label(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
        int[] attrs = {com.google.android.material.R.attr.colorPrimary};
        android.content.res.TypedArray ta = obtainStyledAttributes(attrs);
        tv.setTextColor(ta.getColor(0, 0xFF6750A4));
        ta.recycle();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = px(16); lp.bottomMargin = px(8);
        tv.setLayoutParams(lp);
        return tv;
    }

    private TextView caption(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(13f);
        tv.setAlpha(0.7f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = px(8);
        tv.setLayoutParams(lp);
        return tv;
    }

    private MaterialCardView demoCard() {
        MaterialCardView card = new MaterialCardView(this);
        card.setRadius(dp * 12);
        card.setCardElevation(0f);
        card.setStrokeWidth(px(1));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = px(12);
        card.setLayoutParams(lp);
        return card;
    }

    private MaterialDivider divider(int insetStart) {
        MaterialDivider d = new MaterialDivider(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = insetStart;
        d.setLayoutParams(lp);
        return d;
    }

    private int px(int v) { return (int)(v * dp); }
}
