package com.md3showcase;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

/**
 * MainActivity — MD3 Component Catalog home screen.
 *
 * Sections (each is a live-demo + snippet activity):
 *
 *  🔤 Typography Scale        → TypographyActivity
 *  🔲 Components              → ComponentsActivity
 *     Buttons (all 5 styles), Segmented, Icon Buttons,
 *     FABs, Cards, Text Fields (all variants),
 *     Chips, Selection Controls, Sliders,
 *     Dividers, Side Sheet, Popup Menu
 *
 *  📊 Feedback & Communication → FeedbackActivity
 *     Progress Indicators (linear + circular, det + indet),
 *     Badges, Snackbar (3 variants), Dialogs (4 types), Tooltips
 *
 *  🔝 Top App Bars            → TopAppBarActivity
 *     Small, Center-Aligned, Medium, Large, CollapsingToolbar
 *
 *  📅 Pickers, Tabs & Search  → PickersTabsSearchActivity
 *     Date / Range / Time Pickers, Fixed + Scrollable Tabs,
 *     SearchBar + SearchView
 *
 *  🗂️ Navigation Components   → NavigationDemoActivity
 *     Bottom Nav, Navigation Rail, Navigation Drawer, Popup Menu
 *
 *  📋 List & Card Patterns    → ListPatternsActivity
 *     1-line / 2-line / 3-line lists, Settings list, Horizontal row, Grid
 *
 *  🎠 Carousel & Motion       → CarouselActivity
 *     Material Carousel (multi-browse), Stacked Cards, Shimmer Skeleton
 *
 *  ✨ Advanced UI & Animation → AdvancedActivity
 *     Circular Reveal, Container Morph, Spring Physics,
 *     Draggable Panel, Glassmorphism, Sticky Headers
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean isNightMode;

    // ── Catalog entries: {label, subtitle, targetActivity.class, icon emoji} ──
    private static final Object[][] CATALOG = {
        // Row label          Sub-description                                  Target activity
        {"🔤 Typography",    "15 M3 type roles — Display to Label",           TypographyActivity.class},
        {"🔲 Components",    "Buttons · FABs · Cards · Fields · Chips · Sliders · Sheets", ComponentsActivity.class},
        {"📊 Feedback",      "Progress · Badges · Snackbar · Dialogs · Tooltips", FeedbackActivity.class},
        {"🔝 Top App Bars",  "Small · Center · Medium · Large · Collapsing",  TopAppBarActivity.class},
        {"📅 Pickers & Tabs","Date/Time pickers · Fixed/Scrollable tabs · SearchBar", PickersTabsSearchActivity.class},
        {"🗂️ Navigation",   "Bottom Nav · Rail · Drawer · Popup Menu",       NavigationDemoActivity.class},
        {"📋 List Patterns", "1-line · 2-line · 3-line · Settings · Grid",   ListPatternsActivity.class},
        {"🎠 Carousel",      "Material Carousel · Stacked Cards · Shimmer",   CarouselActivity.class},
        {"✨ Advanced UI",   "Reveal · Morph · Spring · Drag · Glass · Sticky", AdvancedActivity.class},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNightMode = (getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        setContentView(R.layout.main);
        setupToolbar();
        setupNavigationDrawer();
        buildCatalogGrid();
    }

    // ── Toolbar ───────────────────────────────────────────────────────────────

    private void setupToolbar() {
        drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_search) {
                showSnackbar("Use the search bar in Pickers & Tabs section");
                return true;
            }
            if (id == R.id.menu_theme) { toggleTheme(); return true; }
            if (id == R.id.menu_about) { showAbout(); return true; }
            return false;
        });
    }

    // ── Navigation Drawer ─────────────────────────────────────────────────────

    private void setupNavigationDrawer() {
        NavigationView navView = findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            int id = item.getItemId();
            if (id == R.id.nav_carousel)       launch(CarouselActivity.class);
            else if (id == R.id.nav_advanced)  launch(AdvancedActivity.class);
            else if (id == R.id.nav_theme)     toggleTheme();
            else if (id == R.id.nav_dynamic_color) showDynamicColorInfo();
            return true;
        });
    }

    // ── Catalog grid (main content) ───────────────────────────────────────────

    private void buildCatalogGrid() {
        LinearLayout container = findViewById(R.id.content_container);
        if (container == null) return;

        float dp = getResources().getDisplayMetrics().density;
        int pad = (int)(16 * dp);

        // Intro card
        MaterialCardView introCard = new MaterialCardView(this);
        introCard.setRadius(16 * dp);
        introCard.setCardElevation(0f);
        introCard.setStrokeWidth((int)dp);
        LinearLayout.LayoutParams iclp = new LinearLayout.LayoutParams(-1, -2);
        iclp.bottomMargin = (int)(16 * dp);
        introCard.setLayoutParams(iclp);
        LinearLayout introInner = new LinearLayout(this);
        introInner.setOrientation(LinearLayout.VERTICAL);
        introInner.setPadding(pad, pad, pad, pad);
        TextView introTitle = new TextView(this);
        introTitle.setText("Material Design 3 — Complete Reference");
        introTitle.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
        introInner.addView(introTitle);
        TextView introBod = new TextView(this);
        introBod.setText("Every component with a live demo and a copy-paste XML snippet. Tap any category card to explore.");
        introBod.setTextSize(13f);
        introBod.setPadding(0, (int)(6 * dp), 0, 0);
        int[] attrs = {com.google.android.material.R.attr.colorOnSurfaceVariant};
        android.content.res.TypedArray ta = obtainStyledAttributes(attrs);
        introBod.setTextColor(ta.getColor(0, 0xFF49454F));
        ta.recycle();
        introInner.addView(introBod);
        introCard.addView(introInner);
        container.addView(introCard);

        // Section header
        TextView headerTv = new TextView(this);
        headerTv.setText("COMPONENT SECTIONS");
        headerTv.setTextSize(11f);
        headerTv.setLetterSpacing(0.1f);
        headerTv.setAlpha(0.55f);
        LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(-1, -2);
        hlp.bottomMargin = (int)(8 * dp);
        headerTv.setLayoutParams(hlp);
        container.addView(headerTv);

        // Catalog entry cards
        for (Object[] entry : CATALOG) {
            String label    = (String) entry[0];
            String subtitle = (String) entry[1];
            Class<?> target = (Class<?>) entry[2];

            MaterialCardView card = new MaterialCardView(this);
            card.setRadius(14 * dp);
            card.setCardElevation(2 * dp);
            card.setClickable(true);
            card.setFocusable(true);
            LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(-1, -2);
            clp.bottomMargin = (int)(10 * dp);
            card.setLayoutParams(clp);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.setPadding(pad, (int)(14 * dp), pad, (int)(14 * dp));

            LinearLayout textCol = new LinearLayout(this);
            textCol.setOrientation(LinearLayout.VERTICAL);
            textCol.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));

            TextView labelTv = new TextView(this);
            labelTv.setText(label);
            labelTv.setTextSize(16f);
            labelTv.setTypeface(labelTv.getTypeface(), Typeface.BOLD);
            textCol.addView(labelTv);

            TextView subTv = new TextView(this);
            subTv.setText(subtitle);
            subTv.setTextSize(12f);
            subTv.setAlpha(0.6f);
            subTv.setPadding(0, (int)(2 * dp), 0, 0);
            textCol.addView(subTv);

            row.addView(textCol);

            // Chevron
            TextView chevron = new TextView(this);
            chevron.setText("›");
            chevron.setTextSize(22f);
            chevron.setAlpha(0.35f);
            row.addView(chevron);

            card.addView(row);
            card.setOnClickListener(v -> launch(target));
            container.addView(card);
        }

        // Quick-access snippet: Dynamic Color
        TextView dynHeader = new TextView(this);
        dynHeader.setText("SYSTEM INTEGRATION");
        dynHeader.setTextSize(11f);
        dynHeader.setLetterSpacing(0.1f);
        dynHeader.setAlpha(0.55f);
        LinearLayout.LayoutParams dhlp = new LinearLayout.LayoutParams(-1, -2);
        dhlp.topMargin = (int)(8 * dp);
        dhlp.bottomMargin = (int)(8 * dp);
        dynHeader.setLayoutParams(dhlp);
        container.addView(dynHeader);

        MaterialCardView dynCard = new MaterialCardView(this);
        dynCard.setRadius(14 * dp);
        dynCard.setCardElevation(2 * dp);
        dynCard.setClickable(true);
        dynCard.setFocusable(true);
        LinearLayout.LayoutParams dclp = new LinearLayout.LayoutParams(-1, -2);
        dclp.bottomMargin = (int)(10 * dp);
        dynCard.setLayoutParams(dclp);

        LinearLayout dynRow = new LinearLayout(this);
        dynRow.setOrientation(LinearLayout.HORIZONTAL);
        dynRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        dynRow.setPadding(pad, (int)(14 * dp), pad, (int)(14 * dp));

        LinearLayout dynText = new LinearLayout(this);
        dynText.setOrientation(LinearLayout.VERTICAL);
        dynText.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        TextView dynLabel = new TextView(this);
        dynLabel.setText("🎨 Dynamic Color (Material You)");
        dynLabel.setTextSize(16f);
        dynLabel.setTypeface(dynLabel.getTypeface(), Typeface.BOLD);
        dynText.addView(dynLabel);
        TextView dynSub = new TextView(this);
        dynSub.setText("Wallpaper-extracted palette · Android 12+ · DynamicColors API");
        dynSub.setTextSize(12f);
        dynSub.setAlpha(0.6f);
        dynSub.setPadding(0, (int)(2 * dp), 0, 0);
        dynText.addView(dynSub);
        dynRow.addView(dynText);
        TextView dynChev = new TextView(this);
        dynChev.setText("›");
        dynChev.setTextSize(22f);
        dynChev.setAlpha(0.35f);
        dynRow.addView(dynChev);
        dynCard.addView(dynRow);
        dynCard.setOnClickListener(v -> showDynamicColorInfo());
        container.addView(dynCard);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void launch(Class<?> target) {
        startActivity(new Intent(this, target));
    }

    private void toggleTheme() {
        isNightMode = !isNightMode;
        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void showSnackbar(String msg) {
        Snackbar.make(drawerLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showAbout() {
        new MaterialAlertDialogBuilder(this)
            .setTitle("MD3 Component Catalog")
            .setMessage(
                "A complete copy-paste-ready reference for every Material Design 3 component.\n\n" +
                "Each section has:\n" +
                "  • Live interactive demo\n" +
                "  • 'View XML Snippet' button\n" +
                "  • Copy to clipboard\n\n" +
                "Sections:\n" +
                "  🔤 Typography Scale (15 roles)\n" +
                "  🔲 Components (Buttons → Side Sheet)\n" +
                "  📊 Feedback (Progress → Tooltips)\n" +
                "  🔝 Top App Bars (4 variants + Collapsing)\n" +
                "  📅 Pickers · Tabs · Search\n" +
                "  🗂️ Navigation Components\n" +
                "  📋 List & Card Patterns\n" +
                "  🎠 Carousel & Motion\n" +
                "  ✨ Advanced UI & Animation"
            )
            .setPositiveButton("Got it", null)
            .show();
    }

    private void showDynamicColorInfo() {
        showSnippet("Dynamic Color — Material You",
            "// In Application.onCreate():\n" +
            "DynamicColors.applyToActivitiesIfAvailable(this);\n\n" +
            "// Or per-Activity (e.g. in onCreate before setContentView):\n" +
            "DynamicColors.applyToActivityIfAvailable(this);\n\n" +
            "// Dependency:\n" +
            "implementation 'com.google.android.material:material:1.12.0'\n\n" +
            "// How it works:\n" +
            "// Android 12+ (API 31) extracts a color palette from the\n" +
            "// user's wallpaper and maps it to M3 color roles:\n" +
            "//   Primary, Secondary, Tertiary, Error, Surface\n" +
            "// On older devices: falls back to your seed color.\n\n" +
            "// Theme override (force dynamic color in theme):\n" +
            "<style name=\"Theme.App\" parent=\"Theme.Material3.DayNight\">\n" +
            "    <!-- No colorPrimary override needed — DynamicColors\n" +
            "         injects all color tokens automatically. -->\n" +
            "</style>\n\n" +
            "// Custom seed color fallback (values/themes.xml):\n" +
            "<item name=\"colorPrimary\">@color/md_theme_primary</item>"
        );
    }

    private void showSnippet(String title, String code) {
        ScrollView sv = new ScrollView(this);
        TextView tv = new TextView(this);
        int p = (int)(16 * getResources().getDisplayMetrics().density);
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
                showSnackbar("Copied!");
            })
            .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
