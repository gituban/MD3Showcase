package com.md3showcase;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

/**
 * PickersTabsSearchActivity
 *
 * Sections:
 *  1. Date & Time Pickers
 *     • Date picker (single)
 *     • Date range picker
 *     • Time picker (clock / text input toggle)
 *
 *  2. Tabs
 *     • Fixed tabs with icons (3 items)
 *     • Scrollable tabs (5+ items)
 *     • Fixed + ViewPager2 pattern snippet
 *
 *  3. Search Bar + Search View
 *     • Live SearchBar that activates a SearchView overlay
 */
public class PickersTabsSearchActivity extends AppCompatActivity {

    private View rootView;
    private float dp;
    private TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dp = getResources().getDisplayMetrics().density;

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        rootView = root;

        MaterialToolbar toolbar = new MaterialToolbar(this);
        toolbar.setTitle("Pickers, Tabs & Search");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(-1, px(56)));
        root.addView(toolbar);

        NestedScrollView scroll = new NestedScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1f));
        scroll.setClipToPadding(false);
        scroll.setPadding(px(16), px(16), px(16), px(88));

        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);

        // Result label reused across pickers
        resultTv = new TextView(this);
        resultTv.setText("Selected: —");
        resultTv.setTextSize(13f);
        resultTv.setTypeface(Typeface.MONOSPACE);
        resultTv.setPadding(px(12), px(10), px(12), px(10));

        c.addView(sectionLabel("DATE & TIME PICKERS"));
        c.addView(buildPickersSection());

        c.addView(sectionLabel("TABS"));
        c.addView(buildTabsSection());

        c.addView(sectionLabel("SEARCH BAR + SEARCH VIEW"));
        c.addView(buildSearchSection());

        scroll.addView(c);
        root.addView(scroll);
        setContentView(root);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 1. PICKERS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildPickersSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(caption("M3 pickers are modal dialogs — no XML layout needed, fully programmatic."));
        inner.addView(resultTv);

        // Date picker
        inner.addView(subLabel("Date Picker (single)"));
        MaterialButton dateBtn = new MaterialButton(this);
        dateBtn.setText("Select Date");
        dateBtn.setIconResource(R.drawable.ic_calendar);
        dateBtn.setLayoutParams(withTopMargin(px(4)));
        dateBtn.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                    .datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            picker.show(getSupportFragmentManager(), "DATE");
            picker.addOnPositiveButtonClickListener(sel ->
                resultTv.setText("Date: " + picker.getHeaderText()));
        });
        inner.addView(dateBtn);

        // Date range picker
        inner.addView(subLabel("Date Range Picker"));
        MaterialButton rangeBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_TonalButton);
        rangeBtn.setText("Select Date Range");
        rangeBtn.setIconResource(R.drawable.ic_date_range);
        rangeBtn.setLayoutParams(withTopMargin(px(4)));
        rangeBtn.setOnClickListener(v -> {
            MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker =
                    MaterialDatePicker.Builder.dateRangePicker()
                            .setTitleText("Select range")
                            .build();
            picker.show(getSupportFragmentManager(), "DATE_RANGE");
            picker.addOnPositiveButtonClickListener(sel ->
                resultTv.setText("Range: " + picker.getHeaderText()));
        });
        inner.addView(rangeBtn);

        // Time picker
        inner.addView(subLabel("Time Picker"));
        MaterialButton timeBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        timeBtn.setText("Select Time");
        timeBtn.setIconResource(R.drawable.ic_clock);
        timeBtn.setLayoutParams(withTopMargin(px(4)));
        timeBtn.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(DateFormat.is24HourFormat(this)
                            ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H)
                    .setHour(now.get(Calendar.HOUR_OF_DAY))
                    .setMinute(now.get(Calendar.MINUTE))
                    .setTitleText("Select time")
                    .build();
            picker.show(getSupportFragmentManager(), "TIME");
            picker.addOnPositiveButtonClickListener(vv -> {
                String result = String.format("Time: %02d:%02d", picker.getHour(), picker.getMinute());
                resultTv.setText(result);
            });
        });
        inner.addView(timeBtn);

        inner.addView(snippetBtn("Date & Time Pickers",
            "// Date picker:\n" +
            "val picker = MaterialDatePicker.Builder.datePicker()\n" +
            "    .setTitleText(\"Select date\")\n" +
            "    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())\n" +
            "    .build()\n" +
            "picker.show(supportFragmentManager, \"DATE\")\n" +
            "picker.addOnPositiveButtonClickListener { millis ->\n" +
            "    val text = picker.headerText  // formatted date string\n" +
            "}\n\n" +
            "// Date range picker:\n" +
            "val rangePicker = MaterialDatePicker.Builder.dateRangePicker()\n" +
            "    .setTitleText(\"Select range\")\n" +
            "    .build()\n" +
            "rangePicker.show(supportFragmentManager, \"RANGE\")\n" +
            "rangePicker.addOnPositiveButtonClickListener { pair ->\n" +
            "    val start = pair.first   // Long millis\n" +
            "    val end   = pair.second\n" +
            "}\n\n" +
            "// Time picker:\n" +
            "val timePicker = MaterialTimePicker.Builder()\n" +
            "    .setTimeFormat(TimeFormat.CLOCK_12H)\n" +
            "    .setHour(12).setMinute(0)\n" +
            "    .setTitleText(\"Select time\")\n" +
            "    .build()\n" +
            "timePicker.show(supportFragmentManager, \"TIME\")\n" +
            "timePicker.addOnPositiveButtonClickListener {\n" +
            "    val h = timePicker.hour; val m = timePicker.minute\n" +
            "}\n\n" +
            "// Note: No XML layout needed — all dialogs are programmatic."
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 2. TABS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildTabsSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        // Fixed tabs
        inner.addView(subLabel("Fixed tabs with icons (2–4 items)"));
        inner.addView(caption("app:tabMode=\"fixed\" — tabs share space equally. Best for 2–4 tabs."));
        TabLayout fixedTabs = new TabLayout(this);
        fixedTabs.setTabMode(TabLayout.MODE_FIXED);
        fixedTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        fixedTabs.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

        String[] fixedLabels = {"Home", "Explore", "Library"};
        int[] fixedIcons = {R.drawable.ic_home, R.drawable.ic_explore, R.drawable.ic_star};
        for (int i = 0; i < fixedLabels.length; i++) {
            TabLayout.Tab tab = fixedTabs.newTab();
            tab.setText(fixedLabels[i]);
            tab.setIcon(fixedIcons[i]);
            fixedTabs.addTab(tab);
        }
        inner.addView(fixedTabs);

        // Content frame
        FrameLayout contentFrame = new FrameLayout(this);
        contentFrame.setLayoutParams(new LinearLayout.LayoutParams(-1, px(64)));
        contentFrame.setPadding(px(16), px(8), px(16), px(8));
        TextView contentTv = new TextView(this);
        contentTv.setText("Home content");
        contentTv.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
        contentFrame.addView(contentTv);
        inner.addView(contentFrame);

        fixedTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                contentTv.setText(fixedLabels[tab.getPosition()] + " content");
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Scrollable tabs
        inner.addView(subLabel("Scrollable tabs (5+ items)"));
        inner.addView(caption("app:tabMode=\"scrollable\" — tabs scroll horizontally. Best for 4+ tabs."));
        TabLayout scrollTabs = new TabLayout(this);
        scrollTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        scrollTabs.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        String[] scrollLabels = {"All", "Music", "Videos", "Podcasts", "Books", "Games"};
        for (String label : scrollLabels) scrollTabs.addTab(scrollTabs.newTab().setText(label));
        inner.addView(scrollTabs);

        inner.addView(snippetBtn("TabLayout",
            "<!-- Fixed tabs -->\n" +
            "<com.google.android.material.tabs.TabLayout\n" +
            "    android:id=\"@+id/tab_layout\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    app:tabMode=\"fixed\"\n" +
            "    app:tabGravity=\"fill\"\n" +
            "    style=\"@style/Widget.Material3.TabLayout\">\n\n" +
            "    <com.google.android.material.tabs.TabItem\n" +
            "        android:text=\"Home\"\n" +
            "        android:icon=\"@drawable/ic_home\" />\n" +
            "    <com.google.android.material.tabs.TabItem\n" +
            "        android:text=\"Explore\"\n" +
            "        android:icon=\"@drawable/ic_explore\" />\n" +
            "</com.google.android.material.tabs.TabLayout>\n\n" +
            "<!-- Scrollable tabs (4+ items) -->\n" +
            "<com.google.android.material.tabs.TabLayout\n" +
            "    app:tabMode=\"scrollable\" />\n\n" +
            "// Listener:\n" +
            "tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {\n" +
            "    override fun onTabSelected(tab: TabLayout.Tab) {\n" +
            "        // show content for tab.position\n" +
            "    }\n" +
            "    override fun onTabUnselected(tab: TabLayout.Tab) {}\n" +
            "    override fun onTabReselected(tab: TabLayout.Tab) {}\n" +
            "})\n\n" +
            "// With ViewPager2 (auto-sync):\n" +
            "TabLayoutMediator(tabLayout, viewPager2) { tab, position ->\n" +
            "    tab.text = tabTitles[position]\n" +
            "}.attach()"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 3. SEARCH BAR + SEARCH VIEW
    // ══════════════════════════════════════════════════════════════════════════
    private View buildSearchSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(caption(
            "SearchBar is the persistent bar. SearchView is the full-screen overlay.\n" +
            "Tap the bar below to open the search overlay."));

        SearchBar searchBar = new SearchBar(this);
        searchBar.setHint("Search components…");
        searchBar.setNavigationIcon(R.drawable.ic_search);
        LinearLayout.LayoutParams sblp = new LinearLayout.LayoutParams(-1, -2);
        sblp.topMargin = px(8);
        searchBar.setLayoutParams(sblp);
        inner.addView(searchBar);

        // SearchView must be in the same window — we attach it here
        SearchView searchView = new SearchView(this, null,
                com.google.android.material.R.style.Widget_Material3_SearchView);
        searchView.setupWithSearchBar(searchBar);
        LinearLayout.LayoutParams svlp = new LinearLayout.LayoutParams(-1, -2);
        searchView.setLayoutParams(svlp);
        inner.addView(searchView);

        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            String query = searchView.getText().toString();
            searchBar.setText(query);
            searchView.hide();
            Snackbar.make(rootView, "Searched: \"" + query + "\"", Snackbar.LENGTH_SHORT).show();
            return false;
        });

        inner.addView(snippetBtn("SearchBar + SearchView",
            "<!-- In your activity layout -->\n" +
            "<com.google.android.material.search.SearchBar\n" +
            "    android:id=\"@+id/search_bar\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:hint=\"Search…\"\n" +
            "    app:navigationIcon=\"@drawable/ic_search\" />\n\n" +
            "<com.google.android.material.search.SearchView\n" +
            "    android:id=\"@+id/search_view\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    android:hint=\"Search…\" />\n\n" +
            "// Java/Kotlin:\n" +
            "searchView.setupWithSearchBar(searchBar);\n\n" +
            "searchView.editText.setOnEditorActionListener { _, _, _ ->\n" +
            "    val query = searchView.text.toString()\n" +
            "    searchBar.text = query\n" +
            "    searchView.hide()\n" +
            "    // perform search with query\n" +
            "    false\n" +
            "}\n\n" +
            "// Show/hide programmatically:\n" +
            "searchView.show()\n" +
            "searchView.hide()\n\n" +
            "// Note: both must be in the same layout hierarchy."
        ));
        card.addView(inner);
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
        lp.topMargin = px(8);
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

    private TextView subLabel(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelLarge);
        TypedArray ta = obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorOnSurfaceVariant});
        tv.setTextColor(ta.getColor(0, 0xFF49454F));
        ta.recycle();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = px(12);
        lp.bottomMargin = px(4);
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

    private LinearLayout.LayoutParams withTopMargin(int margin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = margin;
        return lp;
    }

    private int px(int v) { return (int)(v * dp); }
}
