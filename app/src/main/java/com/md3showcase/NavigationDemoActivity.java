package com.md3showcase;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.android.material.snackbar.Snackbar;

/**
 * NavigationDemoActivity
 *
 * Demonstrates all M3 navigation components on a single scrollable screen:
 *
 * 1. Bottom Navigation Bar (BottomNavigationView)
 *    – 4-tab live demo with badge on first item
 *    – XML + Kotlin snippet
 *
 * 2. Navigation Rail (NavigationRailView)
 *    – Horizontal layout: rail on left, content frame on right
 *    – 5 destinations with icon + label
 *    – Spec note: 80dp fixed width, 3–7 destinations, FAB header
 *
 * 3. Navigation Drawer (NavigationView embedded)
 *    – Live embedded drawer panel (same widget that slides in a DrawerLayout)
 *    – DrawerLayout XML pattern explanation
 *    – Modal vs Standard drawer explanation
 *
 * 4. Popup Menu
 *    – Anchor-based PopupMenu (overflow pattern)
 *    – Icon button overflow variant
 */
public class NavigationDemoActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView = root;

        MaterialToolbar toolbar = new MaterialToolbar(this);
        toolbar.setTitle("Navigation Components");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int)(56 * dp())));
        root.addView(toolbar);

        NestedScrollView scroll = new NestedScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        scroll.setClipToPadding(false);
        int pad = px(16);
        scroll.setPadding(pad, pad, pad, px(88));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        // ── 1. Bottom Navigation ──────────────────────────────────────────────
        content.addView(sectionLabel("BOTTOM NAVIGATION BAR"));
        content.addView(captionTv(
            "Primary navigation for 3–5 destinations. Stays visible at the bottom " +
            "of the screen. Supports badges via getOrCreateBadge(itemId)."));
        content.addView(buildBottomNavDemo());

        // ── 2. Navigation Rail ────────────────────────────────────────────────
        content.addView(sectionLabel("NAVIGATION RAIL"));
        content.addView(captionTv(
            "Vertical rail for medium-width screens (tablet/foldable). Fixed width 80dp. " +
            "Supports 3–7 destinations. Optional FAB header above items."));
        content.addView(buildNavRailDemo());

        // ── 3. Navigation Drawer ──────────────────────────────────────────────
        content.addView(sectionLabel("NAVIGATION DRAWER"));
        content.addView(captionTv(
            "Slides in from the start edge. Shows full navigation hierarchy. " +
            "Pair with DrawerLayout and trigger via hamburger icon."));
        content.addView(buildNavDrawerDemo());

        // ── 4. Popup Menu ─────────────────────────────────────────────────────
        content.addView(sectionLabel("POPUP / OVERFLOW MENU"));
        content.addView(captionTv("Anchored popup menu triggered from any view — icon button or toolbar overflow."));
        content.addView(buildPopupMenuDemo());

        scroll.addView(content);
        root.addView(scroll);
        setContentView(root);
    }

    // ── Bottom Nav demo ───────────────────────────────────────────────────────

    private View buildBottomNavDemo() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        TextView label = new TextView(this);
        label.setText("Current: Home");
        label.setTextSize(14f);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.bottomMargin = px(8);
        label.setLayoutParams(llp);
        inner.addView(label);

        BottomNavigationView nav = new BottomNavigationView(this);
        nav.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        nav.inflateMenu(R.menu.bottom_nav_menu);
        nav.setOnItemSelectedListener(item -> {
            label.setText("Current: " + item.getTitle());
            return true;
        });
        nav.post(() -> {
            com.google.android.material.badge.BadgeDrawable badge =
                    nav.getOrCreateBadge(R.id.nav_bn_home);
            badge.setNumber(3);
            badge.setVisible(true);
        });
        inner.addView(nav);
        inner.addView(snippetBtn("BottomNavigationView",
            "<!-- layout XML -->\n" +
            "<com.google.android.material.bottomnavigation.BottomNavigationView\n" +
            "    android:id=\"@+id/bottom_nav\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:layout_gravity=\"bottom\"\n" +
            "    app:menu=\"@menu/bottom_nav_menu\" />\n\n" +
            "<!-- menu/bottom_nav_menu.xml -->\n" +
            "<menu>\n" +
            "    <item android:id=\"@+id/nav_home\"\n" +
            "          android:icon=\"@drawable/ic_home\"\n" +
            "          android:title=\"Home\" />\n" +
            "    <item android:id=\"@+id/nav_search\"\n" +
            "          android:icon=\"@drawable/ic_search\"\n" +
            "          android:title=\"Search\" />\n" +
            "</menu>\n\n" +
            "// Kotlin — listener:\n" +
            "bottomNav.setOnItemSelectedListener { item ->\n" +
            "    when (item.itemId) { R.id.nav_home -> true; else -> false }\n" +
            "}\n\n" +
            "// Badge:\n" +
            "bottomNav.getOrCreateBadge(R.id.nav_home).apply {\n" +
            "    number = 5; isVisible = true\n" +
            "}"
        ));
        card.addView(inner);
        return card;
    }

    // ── Nav Rail demo ─────────────────────────────────────────────────────────

    private View buildNavRailDemo() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        LinearLayout railRow = new LinearLayout(this);
        railRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, px(240));
        rlp.bottomMargin = px(8);
        railRow.setLayoutParams(rlp);

        NavigationRailView rail = new NavigationRailView(this);
        LinearLayout.LayoutParams railLp = new LinearLayout.LayoutParams(px(80),
                ViewGroup.LayoutParams.MATCH_PARENT);
        rail.setLayoutParams(railLp);
        rail.inflateMenu(R.menu.nav_rail_demo);
        rail.setLabelVisibilityMode(NavigationRailView.LABEL_VISIBILITY_LABELED);

        FrameLayout contentFrame = new FrameLayout(this);
        contentFrame.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        TextView contentLabel = new TextView(this);
        contentLabel.setText("Home");
        contentLabel.setTextSize(16f);
        contentLabel.setGravity(android.view.Gravity.CENTER);
        contentLabel.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentFrame.addView(contentLabel);

        rail.setOnItemSelectedListener(item -> {
            contentLabel.setText(item.getTitle() + " selected");
            return true;
        });

        railRow.addView(rail);
        railRow.addView(contentFrame);
        inner.addView(railRow);

        inner.addView(snippetBtn("NavigationRailView",
            "<!-- In a horizontal LinearLayout or ConstraintLayout -->\n" +
            "<com.google.android.material.navigationrail.NavigationRailView\n" +
            "    android:id=\"@+id/nav_rail\"\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    app:menu=\"@menu/nav_rail_menu\"\n" +
            "    app:labelVisibilityMode=\"labeled\"\n" +
            "    app:headerLayout=\"@layout/nav_rail_header\" />\n\n" +
            "// Kotlin listener:\n" +
            "navRail.setOnItemSelectedListener { item ->\n" +
            "    // show item.itemId content\n" +
            "    true\n" +
            "}\n\n" +
            "// Spec:\n" +
            "// • Fixed width: 80dp\n" +
            "// • 3–7 destinations\n" +
            "// • Optional FAB in headerLayout above items\n" +
            "// • Use on screens >= 600dp wide"
        ));
        card.addView(inner);
        return card;
    }

    // ── Nav Drawer demo ───────────────────────────────────────────────────────

    private View buildNavDrawerDemo() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(captionTv("Live embedded NavigationView — tap an item:"));

        NavigationView navView = new NavigationView(this);
        navView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, px(240)));
        navView.inflateMenu(R.menu.nav_menu);
        navView.setNavigationItemSelectedListener(item -> {
            Toast.makeText(this, "Selected: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
        inner.addView(navView);

        inner.addView(snippetBtn("DrawerLayout + NavigationView",
            "<!-- activity_main.xml -->\n" +
            "<androidx.drawerlayout.widget.DrawerLayout\n" +
            "    android:id=\"@+id/drawer_layout\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\">\n\n" +
            "    <!-- Main content -->\n" +
            "    <FrameLayout\n" +
            "        android:id=\"@+id/fragment_container\"\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"match_parent\" />\n\n" +
            "    <!-- Navigation Drawer -->\n" +
            "    <com.google.android.material.navigation.NavigationView\n" +
            "        android:id=\"@+id/nav_view\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"match_parent\"\n" +
            "        android:layout_gravity=\"start\"\n" +
            "        app:headerLayout=\"@layout/nav_header\"\n" +
            "        app:menu=\"@menu/nav_menu\" />\n\n" +
            "</androidx.drawerlayout.widget.DrawerLayout>\n\n" +
            "// Java/Kotlin:\n" +
            "toolbar.setNavigationOnClickListener { drawerLayout.open() }\n" +
            "navView.setNavigationItemSelectedListener { item ->\n" +
            "    drawerLayout.close()\n" +
            "    // navigate...\n" +
            "    true\n" +
            "}\n\n" +
            "// Modal (phone): slides over content, dims background.\n" +
            "// Standard (tablet): side-by-side with content, always visible."
        ));
        card.addView(inner);
        return card;
    }

    // ── Popup Menu demo ───────────────────────────────────────────────────────

    private View buildPopupMenuDemo() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(captionTv("Tap the button — menu anchors to it:"));

        MaterialButton anchorBtn = new MaterialButton(this);
        anchorBtn.setText("⋮  Show Popup Menu");
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        blp.topMargin = px(8);
        anchorBtn.setLayoutParams(blp);
        anchorBtn.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popup =
                    new androidx.appcompat.widget.PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(this, "Selected: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });
            popup.show();
        });
        inner.addView(anchorBtn);

        inner.addView(snippetBtn("PopupMenu",
            "// Kotlin:\n" +
            "val popup = PopupMenu(context, anchorView)\n" +
            "popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)\n\n" +
            "popup.setOnMenuItemClickListener { item ->\n" +
            "    when (item.itemId) {\n" +
            "        R.id.action_edit   -> { /* edit   */ true }\n" +
            "        R.id.action_delete -> { /* delete */ true }\n" +
            "        else -> false\n" +
            "    }\n" +
            "}\n" +
            "popup.show()\n\n" +
            "// XML anchor trigger (icon-button overflow):\n" +
            "<com.google.android.material.button.MaterialButton\n" +
            "    style=\"@style/Widget.Material3.Button.IconButton\"\n" +
            "    app:icon=\"@drawable/ic_more_vert\" />"
        ));
        card.addView(inner);
        return card;
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    private MaterialButton snippetBtn(String title, String code) {
        MaterialButton btn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_TextButton);
        btn.setText("View XML Snippet");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = px(8);
        btn.setLayoutParams(lp);
        btn.setOnClickListener(v -> showSnippet(title, code));
        return btn;
    }

    private void showSnippet(String title, String code) {
        ScrollView sv = new ScrollView(this);
        TextView tv = new TextView(this);
        int p = px(16);
        tv.setPadding(p, p, p, p);
        tv.setText(code);
        tv.setTextSize(12f);
        tv.setTypeface(android.graphics.Typeface.MONOSPACE);
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

    private TextView sectionLabel(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
        int[] attrs = {com.google.android.material.R.attr.colorPrimary};
        android.content.res.TypedArray ta = obtainStyledAttributes(attrs);
        tv.setTextColor(ta.getColor(0, 0xFF6750A4));
        ta.recycle();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = px(16);
        lp.bottomMargin = px(8);
        tv.setLayoutParams(lp);
        return tv;
    }

    private TextView captionTv(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(13f);
        int[] attrs = {com.google.android.material.R.attr.colorOnSurfaceVariant};
        android.content.res.TypedArray ta = obtainStyledAttributes(attrs);
        tv.setTextColor(ta.getColor(0, 0xFF49454F));
        ta.recycle();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = px(8);
        tv.setLayoutParams(lp);
        return tv;
    }

    private MaterialCardView demoCard() {
        MaterialCardView card = new MaterialCardView(this);
        card.setRadius(dp() * 12);
        card.setCardElevation(0f);
        card.setStrokeWidth(px(1));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = px(12);
        card.setLayoutParams(lp);
        return card;
    }

    private LinearLayout cardInner() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        int p = px(16);
        ll.setPadding(p, p, p, p);
        return ll;
    }

    private int px(int dp) { return (int)(dp * getResources().getDisplayMetrics().density); }
    private float dp() { return getResources().getDisplayMetrics().density; }
}
