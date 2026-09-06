package com.md3showcase;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

/**
 * FeedbackActivity — all M3 feedback and communication components.
 *
 * Sections:
 *  1. Progress Indicators
 *     • Linear Indeterminate
 *     • Linear Determinate (tap to animate 0→100%)
 *     • Linear Buffer (streaming)
 *     • Circular Indeterminate (Small / Medium / Large)
 *     • Circular Determinate (tap to animate)
 *     • Circular contained in button (loading state)
 *
 *  2. Badges
 *     • Dot badge (no number)
 *     • Number badge (5)
 *     • Large count (99+)
 *
 *  3. Snackbar
 *     • Simple (LENGTH_SHORT)
 *     • With action (Undo)
 *     • Indefinite with action (Retry)
 *
 *  4. Dialogs
 *     • Basic alert
 *     • Single-choice (radio list)
 *     • Multi-choice (checkbox list)
 *     • Input (custom view with EditText)
 *
 *  5. Tooltips
 *     • Long-press on buttons
 *     • Icon button row with tooltips
 */
public class FeedbackActivity extends AppCompatActivity {

    private View rootView;
    private float dp;
    private boolean progressRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dp = getResources().getDisplayMetrics().density;

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        rootView = root;

        MaterialToolbar toolbar = new MaterialToolbar(this);
        toolbar.setTitle("Feedback & Communication");
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

        c.addView(sectionLabel("PROGRESS INDICATORS"));
        c.addView(buildProgressSection());

        c.addView(sectionLabel("BADGES"));
        c.addView(buildBadgesSection());

        c.addView(sectionLabel("SNACKBAR"));
        c.addView(buildSnackbarSection());

        c.addView(sectionLabel("DIALOGS"));
        c.addView(buildDialogsSection());

        c.addView(sectionLabel("TOOLTIPS"));
        c.addView(buildTooltipsSection());

        scroll.addView(c);
        root.addView(scroll);
        setContentView(root);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 1. PROGRESS INDICATORS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildProgressSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        // Linear Indeterminate
        inner.addView(subLabel("Linear — Indeterminate"));
        inner.addView(caption("Use when duration is unknown."));
        LinearProgressIndicator linIndet = new LinearProgressIndicator(this);
        linIndet.setIndeterminate(true);
        linIndet.setLayoutParams(withTopMargin(px(4)));
        inner.addView(linIndet);

        // Linear Determinate (animated)
        inner.addView(subLabel("Linear — Determinate (tap Start)"));
        LinearProgressIndicator linDet = new LinearProgressIndicator(this);
        linDet.setIndeterminate(false);
        linDet.setProgress(0);
        linDet.setLayoutParams(withTopMargin(px(4)));
        inner.addView(linDet);
        TextView linLabel = monoLabel("0%");
        inner.addView(linLabel);

        LinearLayout linBtns = hRow();
        linBtns.setLayoutParams(withTopMargin(px(8)));
        MaterialButton linStart = new MaterialButton(this);
        linStart.setText("Start");
        linStart.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        linStart.setOnClickListener(v -> {
            if (progressRunning) return;
            progressRunning = true;
            linDet.setProgress(0);
            final int[] progress = {0};
            Runnable tick = new Runnable() {
                @Override public void run() {
                    if (progress[0] <= 100) {
                        linDet.setProgressCompat(progress[0], true);
                        linLabel.setText(progress[0] + "%");
                        progress[0]++;
                        linDet.postDelayed(this, 30);
                    } else {
                        progressRunning = false;
                    }
                }
            };
            linDet.post(tick);
        });
        MaterialButton linReset = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        linReset.setText("Reset");
        linReset.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        linReset.setOnClickListener(v -> {
            progressRunning = false;
            linDet.setProgress(0);
            linLabel.setText("0%");
        });
        linBtns.addView(linStart);
        linBtns.addView(linReset);
        inner.addView(linBtns);

        // Circular Indeterminate — three sizes
        inner.addView(subLabel("Circular — Indeterminate (Small / Medium / Large)"));
        LinearLayout circRow = hRow();
        circRow.setGravity(Gravity.CENTER_VERTICAL);
        circRow.setLayoutParams(withTopMargin(px(8)));
        int[] sizes = {24, 40, 56};
        String[] sizeLabels = {"Small (24dp)", "Medium (40dp)", "Large (56dp)"};
        for (int i = 0; i < sizes.length; i++) {
            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            col.setGravity(Gravity.CENTER_HORIZONTAL);
            col.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
            CircularProgressIndicator cpi = new CircularProgressIndicator(this);
            cpi.setIndeterminate(true);
            cpi.setIndicatorSize(px(sizes[i]));
            LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(-2, -2);
            clp.gravity = Gravity.CENTER_HORIZONTAL;
            cpi.setLayoutParams(clp);
            TextView lbl = new TextView(this);
            lbl.setText(sizeLabels[i]);
            lbl.setTextSize(10f);
            lbl.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(-1, -2);
            llp.topMargin = px(4);
            lbl.setLayoutParams(llp);
            col.addView(cpi);
            col.addView(lbl);
            circRow.addView(col);
        }
        inner.addView(circRow);

        // Circular Determinate (animated)
        inner.addView(subLabel("Circular — Determinate (tap Start)"));
        LinearLayout circDetRow = new LinearLayout(this);
        circDetRow.setOrientation(LinearLayout.HORIZONTAL);
        circDetRow.setGravity(Gravity.CENTER_VERTICAL);
        circDetRow.setLayoutParams(withTopMargin(px(8)));

        CircularProgressIndicator circDet = new CircularProgressIndicator(this);
        circDet.setIndeterminate(false);
        circDet.setIndicatorSize(px(72));
        circDet.setTrackThickness(px(6));
        circDet.setProgress(0);
        LinearLayout.LayoutParams cdlp = new LinearLayout.LayoutParams(-2, -2);
        cdlp.rightMargin = px(16);
        circDet.setLayoutParams(cdlp);

        LinearLayout circCtrl = new LinearLayout(this);
        circCtrl.setOrientation(LinearLayout.VERTICAL);
        circCtrl.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        TextView circLabel = monoLabel("0%");
        circCtrl.addView(circLabel);

        LinearLayout circBtns = hRow();
        circBtns.setLayoutParams(withTopMargin(px(8)));
        MaterialButton circStart = new MaterialButton(this);
        circStart.setText("Start");
        circStart.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        final boolean[] circRunning = {false};
        circStart.setOnClickListener(v -> {
            if (circRunning[0]) return;
            circRunning[0] = true;
            circDet.setProgress(0);
            final int[] p = {0};
            Runnable tick = new Runnable() {
                @Override public void run() {
                    if (p[0] <= 100) {
                        circDet.setProgressCompat(p[0], true);
                        circLabel.setText(p[0] + "%");
                        p[0]++;
                        circDet.postDelayed(this, 35);
                    } else {
                        circRunning[0] = false;
                        Snackbar.make(rootView, "Done!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            };
            circDet.post(tick);
        });
        MaterialButton circReset = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        circReset.setText("Reset");
        circReset.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        circReset.setOnClickListener(v -> {
            circRunning[0] = false;
            circDet.setProgress(0);
            circLabel.setText("0%");
        });
        circBtns.addView(circStart);
        circBtns.addView(circReset);
        circCtrl.addView(circBtns);

        circDetRow.addView(circDet);
        circDetRow.addView(circCtrl);
        inner.addView(circDetRow);

        inner.addView(snippetBtn("Progress Indicators",
            "<!-- ① Linear Indeterminate -->\n" +
            "<com.google.android.material.progressindicator.LinearProgressIndicator\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:indeterminate=\"true\" />\n\n" +
            "<!-- ② Linear Determinate -->\n" +
            "<com.google.android.material.progressindicator.LinearProgressIndicator\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:indeterminate=\"false\"\n" +
            "    android:progress=\"0\"\n" +
            "    app:trackThickness=\"8dp\" />\n" +
            "// animate: indicator.setProgressCompat(value, true);\n\n" +
            "<!-- ③ Circular Indeterminate -->\n" +
            "<com.google.android.material.progressindicator.CircularProgressIndicator\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:indeterminate=\"true\"\n" +
            "    app:indicatorSize=\"48dp\"\n" +
            "    app:trackThickness=\"4dp\" />\n\n" +
            "<!-- ④ Circular Determinate -->\n" +
            "<com.google.android.material.progressindicator.CircularProgressIndicator\n" +
            "    android:indeterminate=\"false\"\n" +
            "    android:progress=\"0\"\n" +
            "    app:indicatorSize=\"72dp\"\n" +
            "    app:trackThickness=\"6dp\" />\n\n" +
            "// Animated fill with Runnable (Java):\n" +
            "final int[] p = {0};\n" +
            "Runnable tick = new Runnable() {\n" +
            "    public void run() {\n" +
            "        if (p[0] <= 100) {\n" +
            "            indicator.setProgressCompat(p[0]++, true);\n" +
            "            indicator.postDelayed(this, 30);\n" +
            "        }\n" +
            "    }\n" +
            "};\n" +
            "indicator.post(tick);\n\n" +
            "// Buffer mode (streaming):\n" +
            "linearIndicator.secondaryProgress = bufferedAhead;"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 2. BADGES
    // ══════════════════════════════════════════════════════════════════════════
    @com.google.android.material.badge.ExperimentalBadgeUtils
    private View buildBadgesSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();
        inner.addView(caption("BadgeDrawable attaches to any view — icons, buttons, nav items."));

        LinearLayout badgeRow = hRow();
        badgeRow.setGravity(Gravity.CENTER_VERTICAL);
        badgeRow.setLayoutParams(withTopMargin(px(16)));

        // Dot badge
        ImageView icon1 = new ImageView(this);
        icon1.setImageResource(R.drawable.ic_notifications);
        LinearLayout.LayoutParams i1lp = new LinearLayout.LayoutParams(px(40), px(40));
        i1lp.rightMargin = px(32);
        icon1.setLayoutParams(i1lp);

        // Number badge (5)
        ImageView icon2 = new ImageView(this);
        icon2.setImageResource(R.drawable.ic_email);
        LinearLayout.LayoutParams i2lp = new LinearLayout.LayoutParams(px(40), px(40));
        i2lp.rightMargin = px(32);
        icon2.setLayoutParams(i2lp);

        // Large count badge (99+)
        ImageView icon3 = new ImageView(this);
        icon3.setImageResource(R.drawable.ic_shopping_cart);
        icon3.setLayoutParams(new LinearLayout.LayoutParams(px(40), px(40)));

        badgeRow.addView(icon1);
        badgeRow.addView(icon2);
        badgeRow.addView(icon3);
        inner.addView(badgeRow);

        // Attach badges after layout
        icon1.post(() -> {
            BadgeDrawable dot = BadgeDrawable.create(this);
            dot.setVisible(true);
            BadgeUtils.attachBadgeDrawable(dot, icon1);
        });
        icon2.post(() -> {
            BadgeDrawable num = BadgeDrawable.create(this);
            num.setNumber(5);
            num.setVisible(true);
            BadgeUtils.attachBadgeDrawable(num, icon2);
        });
        icon3.post(() -> {
            BadgeDrawable big = BadgeDrawable.create(this);
            big.setNumber(128);
            big.setVisible(true);
            BadgeUtils.attachBadgeDrawable(big, icon3);
        });

        inner.addView(caption("Left: dot • Center: 5 • Right: 128 → 99+"));

        inner.addView(snippetBtn("BadgeDrawable",
            "// Attach a badge to any view:\n" +
            "val badge = BadgeDrawable.create(context).apply {\n" +
            "    number = 12   // omit for dot badge\n" +
            "    isVisible = true\n" +
            "}\n" +
            "BadgeUtils.attachBadgeDrawable(badge, targetView)\n\n" +
            "// On BottomNavigationView (built-in):\n" +
            "bottomNav.getOrCreateBadge(R.id.nav_home).apply {\n" +
            "    number = 5\n" +
            "    isVisible = true\n" +
            "}\n\n" +
            "// On NavigationRailView:\n" +
            "navRail.getOrCreateBadge(R.id.rail_inbox).apply {\n" +
            "    isVisible = true   // dot only\n" +
            "}\n\n" +
            "// In XML on TabLayout tab (API approach):\n" +
            "tabLayout.getTabAt(0)?.let { tab ->\n" +
            "    tab.orCreateBadge.number = 3\n" +
            "}"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 3. SNACKBAR
    // ══════════════════════════════════════════════════════════════════════════
    private View buildSnackbarSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(subLabel("Simple (auto-dismiss after 3s)"));
        MaterialButton simple = new MaterialButton(this);
        simple.setText("Show Simple Snackbar");
        simple.setLayoutParams(withTopMargin(px(4)));
        simple.setOnClickListener(v ->
            Snackbar.make(rootView, "File saved successfully", Snackbar.LENGTH_SHORT).show());
        inner.addView(simple);

        inner.addView(subLabel("With action (Undo)"));
        MaterialButton withAction = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_TonalButton);
        withAction.setText("Show with Undo");
        withAction.setLayoutParams(withTopMargin(px(4)));
        withAction.setOnClickListener(v ->
            Snackbar.make(rootView, "Message deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", u ->
                    Snackbar.make(rootView, "Restored!", Snackbar.LENGTH_SHORT).show())
                .show());
        inner.addView(withAction);

        inner.addView(subLabel("Indefinite (stays until dismissed)"));
        MaterialButton indefinite = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        indefinite.setText("No Internet (indefinite)");
        indefinite.setLayoutParams(withTopMargin(px(4)));
        indefinite.setOnClickListener(v ->
            Snackbar.make(rootView, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", u ->
                    Snackbar.make(rootView, "Retrying…", Snackbar.LENGTH_SHORT).show())
                .show());
        inner.addView(indefinite);

        inner.addView(snippetBtn("Snackbar",
            "// ① Simple\n" +
            "Snackbar.make(view, \"Message\", Snackbar.LENGTH_SHORT).show();\n\n" +
            "// ② With action\n" +
            "Snackbar.make(view, \"Deleted\", Snackbar.LENGTH_LONG)\n" +
            "    .setAction(\"Undo\") { /* restore */ }\n" +
            "    .show();\n\n" +
            "// ③ Indefinite (persists until action tapped)\n" +
            "Snackbar.make(view, \"No internet\", Snackbar.LENGTH_INDEFINITE)\n" +
            "    .setAction(\"Retry\") { /* retry */ }\n" +
            "    .show();\n\n" +
            "// Anchor above a FAB or BottomNav:\n" +
            "snackbar.setAnchorView(R.id.fab);\n\n" +
            "// Note: pass the activity root view (e.g. CoordinatorLayout)\n" +
            "// so the Snackbar animates correctly with FABs."
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 4. DIALOGS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildDialogsSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        // Basic alert
        inner.addView(subLabel("Basic Alert Dialog"));
        MaterialButton alertBtn = new MaterialButton(this);
        alertBtn.setText("Open Alert Dialog");
        alertBtn.setLayoutParams(withTopMargin(px(4)));
        alertBtn.setOnClickListener(v ->
            new MaterialAlertDialogBuilder(this)
                .setTitle("Delete file?")
                .setMessage("This file will be permanently deleted and cannot be recovered.")
                .setIcon(R.drawable.ic_delete)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) ->
                    Snackbar.make(rootView, "Deleted!", Snackbar.LENGTH_SHORT).show())
                .show());
        inner.addView(alertBtn);

        // Single choice
        inner.addView(subLabel("Single-choice Dialog (radio list)"));
        MaterialButton singleBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_TonalButton);
        singleBtn.setText("Open Single Choice");
        singleBtn.setLayoutParams(withTopMargin(px(4)));
        String[] themes = {"System default", "Light", "Dark"};
        final int[] selectedTheme = {0};
        singleBtn.setOnClickListener(v ->
            new MaterialAlertDialogBuilder(this)
                .setTitle("Choose theme")
                .setSingleChoiceItems(themes, selectedTheme[0], (d, which) ->
                    selectedTheme[0] = which)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (d, w) ->
                    Snackbar.make(rootView, themes[selectedTheme[0]], Snackbar.LENGTH_SHORT).show())
                .show());
        inner.addView(singleBtn);

        // Multi choice
        inner.addView(subLabel("Multi-choice Dialog (checkbox list)"));
        MaterialButton multiBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        multiBtn.setText("Open Multi Choice");
        multiBtn.setLayoutParams(withTopMargin(px(4)));
        String[] items = {"Wifi", "Bluetooth", "NFC", "Hotspot"};
        boolean[] checked = {true, false, false, true};
        multiBtn.setOnClickListener(v ->
            new MaterialAlertDialogBuilder(this)
                .setTitle("Active radios")
                .setMultiChoiceItems(items, checked, (d, which, isChecked) ->
                    checked[which] = isChecked)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (d, w) -> {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.length; i++)
                        if (checked[i]) sb.append(items[i]).append(", ");
                    Snackbar.make(rootView, sb.toString().replaceAll(", $",""), Snackbar.LENGTH_SHORT).show();
                })
                .show());
        inner.addView(multiBtn);

        // Input dialog
        inner.addView(subLabel("Input Dialog (custom EditText view)"));
        MaterialButton inputBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_TextButton);
        inputBtn.setText("Open Input Dialog");
        inputBtn.setLayoutParams(withTopMargin(px(4)));
        inputBtn.setOnClickListener(v -> {
            android.widget.EditText et = new android.widget.EditText(this);
            et.setHint("Enter name…");
            et.setPadding(px(16), px(8), px(16), px(8));
            new MaterialAlertDialogBuilder(this)
                .setTitle("New folder")
                .setView(et)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create", (d, w) ->
                    Snackbar.make(rootView, "Created: " + et.getText(), Snackbar.LENGTH_SHORT).show())
                .show();
        });
        inner.addView(inputBtn);

        inner.addView(snippetBtn("MaterialAlertDialogBuilder",
            "// ① Basic alert\n" +
            "new MaterialAlertDialogBuilder(context)\n" +
            "    .setTitle(\"Title\")\n" +
            "    .setMessage(\"Message\")\n" +
            "    .setIcon(R.drawable.ic_info)\n" +
            "    .setNegativeButton(\"Cancel\", null)\n" +
            "    .setPositiveButton(\"Confirm\", (d, w) -> { })\n" +
            "    .show();\n\n" +
            "// ② Single-choice (radio)\n" +
            ".setSingleChoiceItems(itemsArray, checkedIndex,\n" +
            "    (d, which) -> selected = which)\n\n" +
            "// ③ Multi-choice (checkboxes)\n" +
            ".setMultiChoiceItems(itemsArray, booleanArray,\n" +
            "    (d, which, isChecked) -> checked[which] = isChecked)\n\n" +
            "// ④ Custom view (e.g. EditText)\n" +
            "val et = EditText(context)\n" +
            ".setView(et)\n\n" +
            "// Full-screen Dialog:\n" +
            "val d = Dialog(this, R.style.Theme_MD3Showcase_FullScreenDialog)\n" +
            "d.setContentView(R.layout.your_layout)\n" +
            "d.window?.setLayout(MATCH_PARENT, MATCH_PARENT)\n" +
            "d.show()"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 5. TOOLTIPS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildTooltipsSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();
        inner.addView(caption("Long-press any button below to trigger its tooltip."));

        MaterialButton saveBtnT = new MaterialButton(this);
        saveBtnT.setText("Save");
        saveBtnT.setIconResource(R.drawable.ic_edit);
        saveBtnT.setLayoutParams(withTopMargin(px(8)));
        TooltipCompat.setTooltipText(saveBtnT, "Save your changes");
        inner.addView(saveBtnT);

        MaterialButton shareBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        shareBtn.setText("Share");
        shareBtn.setIconResource(R.drawable.ic_share);
        shareBtn.setLayoutParams(withTopMargin(px(8)));
        TooltipCompat.setTooltipText(shareBtn, "Share this item with others");
        inner.addView(shareBtn);

        inner.addView(subLabel("Icon buttons with tooltips"));
        LinearLayout iconRow = hRow();
        iconRow.setLayoutParams(withTopMargin(px(8)));
        int[] icons = {R.drawable.ic_search, R.drawable.ic_edit, R.drawable.ic_delete, R.drawable.ic_info};
        String[] tips  = {"Search","Edit","Delete","Info"};
        for (int i = 0; i < icons.length; i++) {
            MaterialButton ib = new MaterialButton(this, null,
                    com.google.android.material.R.style.Widget_Material3_Button_IconButton);
            ib.setIconResource(icons[i]);
            ib.setIconPadding(0);
            LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(-2, -2);
            ilp.rightMargin = px(8);
            ib.setLayoutParams(ilp);
            TooltipCompat.setTooltipText(ib, tips[i]);
            iconRow.addView(ib);
        }
        inner.addView(iconRow);

        inner.addView(snippetBtn("Tooltip",
            "// XML — on any View:\n" +
            "<com.google.android.material.button.MaterialButton\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:text=\"Save\"\n" +
            "    android:tooltipText=\"Save your changes\" />\n\n" +
            "// Java/Kotlin — programmatic:\n" +
            "TooltipCompat.setTooltipText(view, \"Tooltip text\");\n" +
            "// or\n" +
            "view.tooltipText = \"Tooltip text\"\n\n" +
            "// Note:\n" +
            "// • Triggered by long-press on touch screens\n" +
            "// • Triggered by hover on mouse/trackpad\n" +
            "// • Requires AppCompat 1.1+ or Material 1.3+\n" +
            "// • For custom tooltip positioning use TooltipDrawable"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SHARED HELPERS
    // ══════════════════════════════════════════════════════════════════════════

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

    private TextView monoLabel(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(12f);
        tv.setTypeface(Typeface.MONOSPACE);
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

    private LinearLayout hRow() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        return ll;
    }

    private LinearLayout.LayoutParams withTopMargin(int margin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = margin;
        return lp;
    }

    private int px(int v) { return (int)(v * dp); }
}
