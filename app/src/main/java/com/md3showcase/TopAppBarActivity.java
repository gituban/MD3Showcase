package com.md3showcase;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

/**
 * TopAppBarActivity
 *
 * Demonstrates all M3 Top App Bar variants with live interactive demos.
 *
 * Variants covered:
 *  1. Small   — fixed height, title in toolbar row
 *  2. Center-Aligned — title centered in toolbar row
 *  3. Medium  — expanded title below toolbar, collapses on scroll
 *  4. Large   — large expanded title, collapses on scroll
 *  5. Collapsing Toolbar — CollapsingToolbarLayout + parallax image
 *
 * Each card shows:
 *  • A live embedded mini-preview of the variant
 *  • Behavior notes
 *  • "View XML Snippet" button → scrollable monospace dialog with full XML
 */
public class TopAppBarActivity extends AppCompatActivity {

    private View rootView;
    private float dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dp = getResources().getDisplayMetrics().density;

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        rootView = root;

        // Host toolbar
        MaterialToolbar hostToolbar = new MaterialToolbar(this);
        hostToolbar.setTitle("Top App Bars");
        hostToolbar.setNavigationIcon(R.drawable.ic_back);
        hostToolbar.setNavigationOnClickListener(v -> finish());
        hostToolbar.setLayoutParams(new LinearLayout.LayoutParams(-1, px(56)));
        root.addView(hostToolbar);

        NestedScrollView scroll = new NestedScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1f));
        scroll.setClipToPadding(false);
        scroll.setPadding(px(16), px(16), px(16), px(88));

        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);

        // 1. Small
        c.addView(sectionLabel("SMALL TOP APP BAR"));
        c.addView(caption("Default. 64dp height. Title aligned start. Navigation icon on the left, action icons on the right."));
        c.addView(buildSmallPreview());
        c.addView(snippetBtn("Small Top App Bar",
            "<!-- activity_main.xml -->\n" +
            "<com.google.android.material.appbar.AppBarLayout\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    app:liftOnScroll=\"true\">\n\n" +
            "    <com.google.android.material.appbar.MaterialToolbar\n" +
            "        android:id=\"@+id/toolbar\"\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"?attr/actionBarSize\"\n" +
            "        app:navigationIcon=\"@drawable/ic_menu\"\n" +
            "        app:menu=\"@menu/top_app_bar_menu\"\n" +
            "        app:title=\"Screen Title\" />\n\n" +
            "</com.google.android.material.appbar.AppBarLayout>\n\n" +
            "// Java:\n" +
            "setSupportActionBar(toolbar);\n" +
            "toolbar.setNavigationOnClickListener(v -> drawerLayout.open());\n" +
            "toolbar.setOnMenuItemClickListener(item -> {\n" +
            "    if (item.getItemId() == R.id.menu_search) { ... return true; }\n" +
            "    return false;\n" +
            "});\n\n" +
            "// liftOnScroll: toolbar gains elevation when content scrolls under it"
        ));

        // 2. Center-Aligned
        c.addView(sectionLabel("CENTER-ALIGNED TOP APP BAR"));
        c.addView(caption("Title is centered. Use app:titleCentered=\"true\" on MaterialToolbar."));
        c.addView(buildCenterPreview());
        c.addView(snippetBtn("Center-Aligned Top App Bar",
            "<com.google.android.material.appbar.MaterialToolbar\n" +
            "    android:id=\"@+id/toolbar\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"?attr/actionBarSize\"\n" +
            "    app:navigationIcon=\"@drawable/ic_menu\"\n" +
            "    app:menu=\"@menu/top_app_bar_menu\"\n" +
            "    app:title=\"Centered Title\"\n" +
            "    app:titleCentered=\"true\" />"
        ));

        // 3. Medium
        c.addView(sectionLabel("MEDIUM TOP APP BAR"));
        c.addView(caption("112dp expanded height. Title sits below the action row and collapses into the toolbar on scroll. Use layout_scrollFlags."));
        c.addView(buildMediumPreview());
        c.addView(snippetBtn("Medium Top App Bar",
            "<com.google.android.material.appbar.AppBarLayout\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\">\n\n" +
            "    <com.google.android.material.appbar.MaterialToolbar\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:minHeight=\"?attr/actionBarSize\"\n" +
            "        app:navigationIcon=\"@drawable/ic_back\"\n" +
            "        app:menu=\"@menu/top_app_bar_menu\"\n" +
            "        app:title=\"Medium Title\"\n" +
            "        app:titleExpanded=\"Medium Title\"\n" +
            "        style=\"@style/Widget.Material3.Toolbar.Medium\"\n" +
            "        app:layout_scrollFlags=\"scroll|exitUntilCollapsed|snap\" />\n\n" +
            "</com.google.android.material.appbar.AppBarLayout>\n\n" +
            "// Pair with CoordinatorLayout + NestedScrollView:\n" +
            "// nestedScrollView: app:layout_behavior=\"@string/appbar_scrolling_view_behavior\""
        ));

        // 4. Large
        c.addView(sectionLabel("LARGE TOP APP BAR"));
        c.addView(caption("152dp expanded height. Larger title that collapses. Best for top-level screens."));
        c.addView(buildLargePreview());
        c.addView(snippetBtn("Large Top App Bar",
            "<com.google.android.material.appbar.AppBarLayout\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\">\n\n" +
            "    <com.google.android.material.appbar.MaterialToolbar\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:minHeight=\"?attr/actionBarSize\"\n" +
            "        app:navigationIcon=\"@drawable/ic_back\"\n" +
            "        app:title=\"Large Title\"\n" +
            "        style=\"@style/Widget.Material3.Toolbar.Large\"\n" +
            "        app:layout_scrollFlags=\"scroll|exitUntilCollapsed|snap\" />\n\n" +
            "</com.google.android.material.appbar.AppBarLayout>"
        ));

        // 5. Collapsing Toolbar
        c.addView(sectionLabel("COLLAPSING TOOLBAR (parallax image)"));
        c.addView(caption("CollapsingToolbarLayout — title scales down and image parallax-scrolls as user scrolls. Used in MainActivity here."));
        c.addView(snippetBtn("CollapsingToolbarLayout",
            "<androidx.coordinatorlayout.widget.CoordinatorLayout>\n\n" +
            "    <com.google.android.material.appbar.AppBarLayout\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"200dp\"\n" +
            "        android:fitsSystemWindows=\"true\">\n\n" +
            "        <com.google.android.material.appbar.CollapsingToolbarLayout\n" +
            "            android:layout_width=\"match_parent\"\n" +
            "            android:layout_height=\"match_parent\"\n" +
            "            app:contentScrim=\"?attr/colorSurface\"\n" +
            "            app:title=\"Screen Title\"\n" +
            "            app:titleCollapseMode=\"scale\"\n" +
            "            app:layout_scrollFlags=\"scroll|exitUntilCollapsed|snap\">\n\n" +
            "            <!-- Parallax image -->\n" +
            "            <ImageView\n" +
            "                android:layout_width=\"match_parent\"\n" +
            "                android:layout_height=\"match_parent\"\n" +
            "                android:scaleType=\"centerCrop\"\n" +
            "                android:src=\"@drawable/header_gradient\"\n" +
            "                app:layout_collapseMode=\"parallax\"\n" +
            "                app:layout_collapseParallaxMultiplier=\"0.7\" />\n\n" +
            "            <!-- Pinned toolbar -->\n" +
            "            <com.google.android.material.appbar.MaterialToolbar\n" +
            "                android:layout_width=\"match_parent\"\n" +
            "                android:layout_height=\"?attr/actionBarSize\"\n" +
            "                android:layout_gravity=\"bottom\"\n" +
            "                app:layout_collapseMode=\"pin\"\n" +
            "                app:navigationIcon=\"@drawable/ic_menu\"\n" +
            "                app:menu=\"@menu/main_menu\" />\n\n" +
            "        </com.google.android.material.appbar.CollapsingToolbarLayout>\n" +
            "    </com.google.android.material.appbar.AppBarLayout>\n\n" +
            "    <androidx.core.widget.NestedScrollView\n" +
            "        app:layout_behavior=\"@string/appbar_scrolling_view_behavior\">\n" +
            "        <!-- content -->\n" +
            "    </androidx.core.widget.NestedScrollView>\n\n" +
            "</androidx.coordinatorlayout.widget.CoordinatorLayout>\n\n" +
            "// titleCollapseMode options: fade | scale\n" +
            "// layout_collapseMode options: pin | parallax | none"
        ));

        scroll.addView(c);
        root.addView(scroll);
        setContentView(root);
    }

    // ── Preview builders ──────────────────────────────────────────────────────

    private View buildSmallPreview() {
        MaterialCardView card = demoCard();
        MaterialToolbar tb = new MaterialToolbar(this);
        tb.setTitle("Screen Title");
        tb.setNavigationIcon(R.drawable.ic_menu);
        tb.setLayoutParams(new LinearLayout.LayoutParams(-1, px(56)));
        tb.setNavigationOnClickListener(v ->
            Snackbar.make(rootView, "Navigation icon tapped", Snackbar.LENGTH_SHORT).show());
        tb.inflateMenu(R.menu.top_app_bar_menu);
        tb.setOnMenuItemClickListener(item -> {
            Snackbar.make(rootView, item.getTitle().toString(), Snackbar.LENGTH_SHORT).show();
            return true;
        });
        card.addView(tb);
        return card;
    }

    private View buildCenterPreview() {
        MaterialCardView card = demoCard();
        MaterialToolbar tb = new MaterialToolbar(this);
        tb.setTitle("Centered Title");
        tb.setTitleCentered(true);
        tb.setNavigationIcon(R.drawable.ic_back);
        tb.setLayoutParams(new LinearLayout.LayoutParams(-1, px(56)));
        tb.setNavigationOnClickListener(v ->
            Snackbar.make(rootView, "Back tapped", Snackbar.LENGTH_SHORT).show());
        tb.inflateMenu(R.menu.top_app_bar_menu);
        card.addView(tb);
        return card;
    }

    private View buildMediumPreview() {
        MaterialCardView card = demoCard();
        LinearLayout preview = new LinearLayout(this);
        preview.setOrientation(LinearLayout.VERTICAL);

        // Icon row
        MaterialToolbar iconRow = new MaterialToolbar(this);
        iconRow.setNavigationIcon(R.drawable.ic_back);
        iconRow.setLayoutParams(new LinearLayout.LayoutParams(-1, px(56)));
        iconRow.inflateMenu(R.menu.top_app_bar_menu);
        preview.addView(iconRow);

        // Expanded title below
        TextView expandedTitle = new TextView(this);
        expandedTitle.setText("Medium App Bar");
        expandedTitle.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_HeadlineSmall);
        expandedTitle.setPadding(px(16), px(4), px(16), px(12));
        preview.addView(expandedTitle);

        card.addView(preview);
        return card;
    }

    private View buildLargePreview() {
        MaterialCardView card = demoCard();
        LinearLayout preview = new LinearLayout(this);
        preview.setOrientation(LinearLayout.VERTICAL);

        MaterialToolbar iconRow = new MaterialToolbar(this);
        iconRow.setNavigationIcon(R.drawable.ic_back);
        iconRow.setLayoutParams(new LinearLayout.LayoutParams(-1, px(56)));
        iconRow.inflateMenu(R.menu.top_app_bar_menu);
        preview.addView(iconRow);

        TextView expandedTitle = new TextView(this);
        expandedTitle.setText("Large App Bar Title");
        expandedTitle.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_HeadlineMedium);
        expandedTitle.setPadding(px(16), px(4), px(16), px(16));
        preview.addView(expandedTitle);

        card.addView(preview);
        return card;
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    private void showSnippet(String title, String code) {
        ScrollView sv = new ScrollView(this);
        TextView tv = new TextView(this);
        int p = px(16);
        tv.setPadding(p, p, p, p);
        tv.setText(code);
        tv.setTextSize(12f);
        tv.setTypeface(Typeface.MONOSPACE);
        sv.addView(tv);
        new MaterialAlertDialogBuilder(this)
                .setTitle("📋  " + title)
                .setView(sv)
                .setPositiveButton("Close", null)
                .setNeutralButton("Copy", (d, w) -> {
                    android.content.ClipboardManager cm =
                            (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(android.content.ClipData.newPlainText("snippet", code));
                    Snackbar.make(rootView, "Copied!", Snackbar.LENGTH_SHORT).show();
                })
                .show();
    }

    private MaterialButton snippetBtn(String title, String code) {
        MaterialButton btn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_TextButton);
        btn.setText("View XML Snippet");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
        lp.topMargin = px(4);
        lp.bottomMargin = px(8);
        btn.setLayoutParams(lp);
        btn.setOnClickListener(v -> showSnippet(title, code));
        return btn;
    }

    private TextView sectionLabel(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
        TypedArray ta = obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorPrimary});
        tv.setTextColor(ta.getColor(0, 0xFF6750A4));
        ta.recycle();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = px(16);
        lp.bottomMargin = px(8);
        tv.setLayoutParams(lp);
        return tv;
    }

    private TextView caption(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(13f);
        TypedArray ta = obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorOnSurfaceVariant});
        tv.setTextColor(ta.getColor(0, 0xFF49454F));
        ta.recycle();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = px(8);
        tv.setLayoutParams(lp);
        return tv;
    }

    private MaterialCardView demoCard() {
        MaterialCardView card = new MaterialCardView(this);
        card.setRadius(dp * 12);
        card.setCardElevation(0f);
        card.setStrokeWidth(px(1));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = px(4);
        card.setLayoutParams(lp);
        return card;
    }

    private int px(int v) { return (int)(v * dp); }
}
