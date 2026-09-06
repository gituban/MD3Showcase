package com.md3showcase;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.sidesheet.SideSheetDialog;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * ComponentsActivity — every M3 component with live demo + XML snippet.
 *
 * Sections:
 *  1.  Buttons (Filled/Tonal/Outlined/Text/Elevated + Icon btn all 4 styles)
 *  2.  Segmented Buttons (single-select / multi-select / icon-only)
 *  3.  FABs (Small / Standard / Large / Extended + shrink/extend control)
 *  4.  Cards (Elevated / Filled / Outlined / Selectable)
 *  5.  Text Fields (Filled / Outlined / Dense / Password / Error / Counter / Dropdown)
 *  6.  Chips (Assist / Filter / Input / Suggestion)
 *  7.  Selection Controls (Checkboxes incl. parent/child + Radio + Switch)
 *  8.  Sliders (Continuous / Discrete / Range)
 *  9.  Dividers (Full-width / Inset / Vertical)
 * 10.  Side Sheet (Modal SideSheetDialog with filter panel)
 * 11.  Popup Menu (anchor-based + overflow icon button)
 */
public class ComponentsActivity extends AppCompatActivity {

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

        MaterialToolbar toolbar = new MaterialToolbar(this);
        toolbar.setTitle("Components");
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

        // ── 1. BUTTONS ────────────────────────────────────────────────────────
        c.addView(sectionLabel("BUTTONS"));
        c.addView(caption("All 5 button styles — tap any to see its XML snippet."));
        c.addView(buildButtonsSection());

        // ── 2. SEGMENTED BUTTONS ──────────────────────────────────────────────
        c.addView(sectionLabel("SEGMENTED BUTTONS"));
        c.addView(caption("MaterialButtonToggleGroup — single-select, multi-select, and icon-only variants."));
        c.addView(buildSegmentedButtons());

        // ── 3. FABs ───────────────────────────────────────────────────────────
        c.addView(sectionLabel("FLOATING ACTION BUTTONS"));
        c.addView(caption("Small / Standard / Large / Extended — tap each to see snippet."));
        c.addView(buildFABs());

        // ── 4. CARDS ──────────────────────────────────────────────────────────
        c.addView(sectionLabel("CARDS"));
        c.addView(caption("Elevated / Filled / Outlined / Selectable (checkable=true)."));
        c.addView(buildCards());

        // ── 5. TEXT FIELDS ────────────────────────────────────────────────────
        c.addView(sectionLabel("TEXT FIELDS"));
        c.addView(caption("All TextInputLayout styles with error, counter, password, dropdown variants."));
        c.addView(buildTextFields());

        // ── 6. CHIPS ──────────────────────────────────────────────────────────
        c.addView(sectionLabel("CHIPS"));
        c.addView(caption("Assist / Filter / Input (closeable) / Suggestion — tap for snippet."));
        c.addView(buildChips());

        // ── 7. SELECTION CONTROLS ─────────────────────────────────────────────
        c.addView(sectionLabel("SELECTION CONTROLS"));
        c.addView(caption("Checkboxes (incl. parent/child), Radio Buttons, Switches."));
        c.addView(buildSelectionControls());

        // ── 8. SLIDERS ────────────────────────────────────────────────────────
        c.addView(sectionLabel("SLIDERS"));
        c.addView(caption("Continuous / Discrete (stepped) / Range (two thumbs)."));
        c.addView(buildSliders());

        // ── 9. DIVIDERS ───────────────────────────────────────────────────────
        c.addView(sectionLabel("DIVIDERS"));
        c.addView(caption("Full-width, inset, and vertical MaterialDivider."));
        c.addView(buildDividers());

        // ── 10. SIDE SHEET ────────────────────────────────────────────────────
        c.addView(sectionLabel("SIDE SHEET"));
        c.addView(caption("SideSheetDialog — slides from the right edge. Filter panel demo."));
        c.addView(buildSideSheet());

        // ── 11. POPUP MENU ────────────────────────────────────────────────────
        c.addView(sectionLabel("POPUP / OVERFLOW MENU"));
        c.addView(caption("Anchor-based PopupMenu and icon-button overflow pattern."));
        c.addView(buildPopupMenu());

        scroll.addView(c);
        root.addView(scroll);
        setContentView(root);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 1. BUTTONS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildButtonsSection() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        // Row 1: Filled / Tonal / Outlined
        LinearLayout row1 = hRow();
        String[][] styles = {
            {"Filled",   ""},
            {"Tonal",    ".TonalButton"},
            {"Outlined", ".OutlinedButton"},
        };
        for (String[] s : styles) {
            MaterialButton btn = new MaterialButton(this);
            btn.setText(s[0]);
            btn.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
            String xmlStyle = "Widget.Material3.Button" + s[1];
            String finalStyle = s[1];
            btn.setOnClickListener(v -> showSnippet("MaterialButton — " + s[0],
                "<!-- " + s[0] + " button -->\n" +
                "<com.google.android.material.button.MaterialButton\n" +
                "    style=\"@style/" + xmlStyle + "\"\n" +
                "    android:layout_width=\"wrap_content\"\n" +
                "    android:layout_height=\"wrap_content\"\n" +
                "    android:text=\"" + s[0] + "\" />"
            ));
            row1.addView(btn);
        }
        inner.addView(row1);

        // Row 2: Text / Elevated / Icon
        LinearLayout row2 = hRow();
        row2.setLayoutParams(withTopMargin(px(8)));
        String[][] styles2 = {
            {"Text",     ".TextButton"},
            {"Elevated", ".ElevatedButton"},
        };
        for (String[] s : styles2) {
            MaterialButton btn = new MaterialButton(this);
            btn.setText(s[0]);
            btn.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
            btn.setOnClickListener(v -> showSnippet("MaterialButton — " + s[0],
                "<com.google.android.material.button.MaterialButton\n" +
                "    style=\"@style/Widget.Material3.Button" + s[1] + "\"\n" +
                "    android:text=\"" + s[0] + "\" />"
            ));
            row2.addView(btn);
        }
        MaterialButton iconBtn = new MaterialButton(this);
        iconBtn.setText("Icon");
        iconBtn.setIconResource(R.drawable.ic_star);
        iconBtn.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        iconBtn.setOnClickListener(v -> showSnippet("MaterialButton — with icon",
            "<com.google.android.material.button.MaterialButton\n" +
            "    style=\"@style/Widget.Material3.Button.TonalButton\"\n" +
            "    android:text=\"Add\"\n" +
            "    app:icon=\"@drawable/ic_add\" />"
        ));
        row2.addView(iconBtn);
        inner.addView(row2);

        // Icon button row: Standard / Filled / FilledTonal / Outlined
        inner.addView(subLabel("Icon Buttons"));
        LinearLayout iconRow = hRow();
        int[][] iconStyles = {
            {com.google.android.material.R.style.Widget_Material3_Button_IconButton},
            {com.google.android.material.R.style.Widget_Material3_Button_IconButton_Filled},
            {com.google.android.material.R.style.Widget_Material3_Button_IconButton_Filled},
            {com.google.android.material.R.style.Widget_Material3_Button_IconButton_Outlined},
        };
        String[] iconStyleNames = {"IconButton", "IconButton.Filled", "IconButton.Filled", "IconButton.Outlined"};
        for (int i = 0; i < iconStyles.length; i++) {
            MaterialButton ib = new MaterialButton(this, null, 0);
            ib.setTextAppearance(iconStyles[i][0]);
            try {
                ib = new MaterialButton(this, null, iconStyles[i][0]);
            } catch (Exception ignored) {}
            ib.setIconResource(R.drawable.ic_star);
            ib.setText("");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
            lp.rightMargin = px(8);
            ib.setLayoutParams(lp);
            String styleName = iconStyleNames[i];
            ib.setOnClickListener(v -> showSnippet("Icon Button — " + styleName,
                "<com.google.android.material.button.MaterialButton\n" +
                "    style=\"@style/Widget.Material3.Button." + styleName + "\"\n" +
                "    android:layout_width=\"wrap_content\"\n" +
                "    android:layout_height=\"wrap_content\"\n" +
                "    app:icon=\"@drawable/ic_star\" />"
            ));
            iconRow.addView(ib);
        }
        inner.addView(iconRow);

        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 2. SEGMENTED BUTTONS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildSegmentedButtons() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(subLabel("Single-select (Day / Week / Month)"));
        MaterialButtonToggleGroup single = new MaterialButtonToggleGroup(this);
        single.setSingleSelection(true);
        single.setSelectionRequired(true);
        single.setLayoutParams(withTopMargin(px(4)));
        String[] singles = {"Day", "Week", "Month"};
        for (String s : singles) {
            MaterialButton b = new MaterialButton(this, null,
                    com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
            b.setText(s);
            b.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
            single.addView(b);
        }
        single.check(((MaterialButton)single.getChildAt(0)).getId());
        single.addOnButtonCheckedListener((g, id, checked) -> {
            if (checked) {
                MaterialButton b = g.findViewById(id);
                if (b != null) Toast.makeText(this, "Selected: " + b.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        inner.addView(single);

        inner.addView(subLabel("Multi-select (Bold / Italic / Underline)"));
        MaterialButtonToggleGroup multi = new MaterialButtonToggleGroup(this);
        multi.setSingleSelection(false);
        multi.setLayoutParams(withTopMargin(px(4)));
        for (String s : new String[]{"Bold", "Italic", "Underline"}) {
            MaterialButton b = new MaterialButton(this, null,
                    com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
            b.setText(s);
            b.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
            multi.addView(b);
        }
        inner.addView(multi);

        inner.addView(subLabel("Icon-only (List / Grid / Gallery)"));
        MaterialButtonToggleGroup iconGroup = new MaterialButtonToggleGroup(this);
        iconGroup.setSingleSelection(true);
        iconGroup.setSelectionRequired(true);
        LinearLayout.LayoutParams iglp = new LinearLayout.LayoutParams(-2, -2);
        iglp.topMargin = px(4);
        iconGroup.setLayoutParams(iglp);
        int[] icons = {R.drawable.ic_home, R.drawable.ic_star, R.drawable.ic_palette};
        for (int icon : icons) {
            MaterialButton b = new MaterialButton(this, null,
                    com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
            b.setIconResource(icon);
            b.setText("");
            b.setIconPadding(0);
            b.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            iconGroup.addView(b);
        }
        iconGroup.check(((MaterialButton)iconGroup.getChildAt(0)).getId());
        inner.addView(iconGroup);

        inner.addView(snippetBtn("MaterialButtonToggleGroup",
            "<!-- Single-select segmented button -->\n" +
            "<com.google.android.material.button.MaterialButtonToggleGroup\n" +
            "    android:id=\"@+id/toggle_group\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    app:singleSelection=\"true\"\n" +
            "    app:selectionRequired=\"true\">\n\n" +
            "    <com.google.android.material.button.MaterialButton\n" +
            "        style=\"@style/Widget.Material3.Button.OutlinedButton\"\n" +
            "        android:layout_width=\"0dp\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_weight=\"1\"\n" +
            "        android:text=\"Day\" />\n\n" +
            "    <!-- repeat for each option -->\n" +
            "</com.google.android.material.button.MaterialButtonToggleGroup>\n\n" +
            "// Java/Kotlin listener:\n" +
            "toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->\n" +
            "    if (isChecked) {\n" +
            "        val btn = group.findViewById<MaterialButton>(checkedId)\n" +
            "        // use btn.text\n" +
            "    }\n" +
            "}\n\n" +
            "// Multi-select: app:singleSelection=\"false\"\n" +
            "// Icon-only: set app:icon, no android:text\n" +
            "// Get checked IDs: toggleGroup.checkedButtonIds"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 3. FABs
    // ══════════════════════════════════════════════════════════════════════════
    private View buildFABs() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        LinearLayout fabRow = hRow();
        fabRow.setGravity(Gravity.CENTER_VERTICAL);

        // Small
        FloatingActionButton fabSmall = new FloatingActionButton(this);
        fabSmall.setImageResource(R.drawable.ic_add);
        fabSmall.setSize(FloatingActionButton.SIZE_MINI);
        LinearLayout.LayoutParams fslp = new LinearLayout.LayoutParams(-2, -2);
        fslp.rightMargin = px(16);
        fabSmall.setLayoutParams(fslp);
        fabSmall.setOnClickListener(v -> showSnippet("FAB — Small",
            "<com.google.android.material.floatingactionbutton.FloatingActionButton\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:src=\"@drawable/ic_add\"\n" +
            "    app:fabSize=\"mini\" />"));
        fabRow.addView(fabSmall);

        // Standard
        FloatingActionButton fabNormal = new FloatingActionButton(this);
        fabNormal.setImageResource(R.drawable.ic_add);
        fabNormal.setSize(FloatingActionButton.SIZE_NORMAL);
        LinearLayout.LayoutParams fnlp = new LinearLayout.LayoutParams(-2, -2);
        fnlp.rightMargin = px(16);
        fabNormal.setLayoutParams(fnlp);
        fabNormal.setOnClickListener(v -> showSnippet("FAB — Standard",
            "<com.google.android.material.floatingactionbutton.FloatingActionButton\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:src=\"@drawable/ic_add\"\n" +
            "    app:fabSize=\"normal\" />"));
        fabRow.addView(fabNormal);

        // Large
        FloatingActionButton fabLarge = new FloatingActionButton(this);
        fabLarge.setImageResource(R.drawable.ic_add);
        fabLarge.setSize(FloatingActionButton.SIZE_AUTO);
        fabLarge.setCustomSize(px(72));
        LinearLayout.LayoutParams fllp = new LinearLayout.LayoutParams(-2, -2);
        fllp.rightMargin = px(16);
        fabLarge.setLayoutParams(fllp);
        fabLarge.setOnClickListener(v -> showSnippet("FAB — Large",
            "<!-- Use style or customSize -->\n" +
            "<com.google.android.material.floatingactionbutton.FloatingActionButton\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:src=\"@drawable/ic_add\"\n" +
            "    app:fabSize=\"large\" />"));
        fabRow.addView(fabLarge);

        inner.addView(fabRow);

        inner.addView(subLabel("Extended FAB (shrink/extend)"));
        ExtendedFloatingActionButton extFab = new ExtendedFloatingActionButton(this);
        extFab.setText("Compose");
        extFab.setIconResource(R.drawable.ic_edit);
        extFab.setLayoutParams(withTopMargin(px(8)));
        extFab.setOnClickListener(v -> showSnippet("ExtendedFloatingActionButton",
            "<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton\n" +
            "    android:id=\"@+id/extended_fab\"\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:layout_gravity=\"bottom|end\"\n" +
            "    android:layout_margin=\"16dp\"\n" +
            "    android:text=\"Compose\"\n" +
            "    app:icon=\"@drawable/ic_edit\" />\n\n" +
            "// Shrink/extend on scroll:\n" +
            "nestedScrollView.setOnScrollChangeListener(\n" +
            "    (v, scrollX, scrollY, oldX, oldY) -> {\n" +
            "        if (scrollY > oldY) extFab.shrink();\n" +
            "        else extFab.extend();\n" +
            "    }\n" +
            ");\n\n" +
            "// CoordinatorLayout anchor to AppBarLayout:\n" +
            "app:layout_anchor=\"@id/app_bar\"\n" +
            "app:layout_anchorGravity=\"bottom|end\""
        ));
        inner.addView(extFab);

        LinearLayout ctrlRow = hRow();
        ctrlRow.setLayoutParams(withTopMargin(px(8)));
        MaterialButton shrinkBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        shrinkBtn.setText("Shrink");
        shrinkBtn.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        shrinkBtn.setOnClickListener(v -> extFab.shrink());
        MaterialButton extendBtn = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
        extendBtn.setText("Extend");
        extendBtn.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
        extendBtn.setOnClickListener(v -> extFab.extend());
        ctrlRow.addView(shrinkBtn);
        ctrlRow.addView(extendBtn);
        inner.addView(ctrlRow);

        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 4. CARDS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildCards() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        LinearLayout row = hRow();

        // Elevated
        MaterialCardView elevated = new MaterialCardView(this);
        elevated.setRadius(dp * 12);
        elevated.setCardElevation(dp * 3);
        elevated.setLayoutParams(new LinearLayout.LayoutParams(0, px(100), 1f));
        elevated.addView(centeredLabel("Elevated"));
        elevated.setOnClickListener(v -> showSnippet("Card — Elevated",
            "<com.google.android.material.card.MaterialCardView\n" +
            "    style=\"@style/Widget.Material3.CardView.Elevated\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    app:cardElevation=\"3dp\">\n" +
            "    <!-- content -->\n" +
            "</com.google.android.material.card.MaterialCardView>"));
        row.addView(elevated);

        // Filled
        MaterialCardView filled = new MaterialCardView(this, null,
                com.google.android.material.R.style.Widget_Material3_CardView_Filled);
        filled.setRadius(dp * 12);
        filled.setCardElevation(0f);
        LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(0, px(100), 1f);
        flp.leftMargin = px(8);
        filled.setLayoutParams(flp);
        filled.addView(centeredLabel("Filled"));
        filled.setOnClickListener(v -> showSnippet("Card — Filled",
            "<com.google.android.material.card.MaterialCardView\n" +
            "    style=\"@style/Widget.Material3.CardView.Filled\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    app:cardElevation=\"0dp\">\n" +
            "    <!-- content -->\n" +
            "</com.google.android.material.card.MaterialCardView>"));
        row.addView(filled);

        // Outlined
        MaterialCardView outlined = new MaterialCardView(this, null,
                com.google.android.material.R.style.Widget_Material3_CardView_Outlined);
        outlined.setRadius(dp * 12);
        outlined.setCardElevation(0f);
        outlined.setStrokeWidth(px(1));
        LinearLayout.LayoutParams olp = new LinearLayout.LayoutParams(0, px(100), 1f);
        olp.leftMargin = px(8);
        outlined.setLayoutParams(olp);
        outlined.addView(centeredLabel("Outlined"));
        outlined.setOnClickListener(v -> showSnippet("Card — Outlined",
            "<com.google.android.material.card.MaterialCardView\n" +
            "    style=\"@style/Widget.Material3.CardView.Outlined\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    app:cardElevation=\"0dp\"\n" +
            "    app:strokeWidth=\"1dp\">\n" +
            "    <!-- content -->\n" +
            "</com.google.android.material.card.MaterialCardView>"));
        row.addView(outlined);
        inner.addView(row);

        // Selectable
        inner.addView(subLabel("Selectable (checkable=true)"));
        MaterialCardView selectable = new MaterialCardView(this);
        selectable.setRadius(dp * 12);
        selectable.setCardElevation(dp * 2);
        selectable.setCheckable(true);
        selectable.setClickable(true);
        selectable.setFocusable(true);
        selectable.setLayoutParams(withTopMargin(px(8)));
        LinearLayout selInner = new LinearLayout(this);
        selInner.setOrientation(LinearLayout.VERTICAL);
        selInner.setPadding(px(16), px(12), px(16), px(12));
        TextView selTv = new TextView(this);
        selTv.setText("Tap to toggle selection state");
        selTv.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
        selInner.addView(selTv);
        selectable.addView(selInner);
        selectable.setOnClickListener(v -> {
            selectable.setChecked(!selectable.isChecked());
            selTv.setText(selectable.isChecked() ? "✓ Selected" : "Tap to toggle selection state");
        });
        inner.addView(selectable);

        inner.addView(snippetBtn("Card — Selectable",
            "<!-- Selectable / checkable card -->\n" +
            "<com.google.android.material.card.MaterialCardView\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:clickable=\"true\"\n" +
            "    android:focusable=\"true\"\n" +
            "    app:checkable=\"true\"\n" +
            "    app:cardElevation=\"2dp\">\n" +
            "    <!-- content -->\n" +
            "</com.google.android.material.card.MaterialCardView>\n\n" +
            "// Java/Kotlin:\n" +
            "card.setOnClickListener {\n" +
            "    card.isChecked = !card.isChecked\n" +
            "}"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 5. TEXT FIELDS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildTextFields() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        // Filled
        inner.addView(subLabel("Filled (with leading icon + counter)"));
        TextInputLayout tilFilled = new TextInputLayout(this, null,
                com.google.android.material.R.style.Widget_Material3_TextInputLayout_FilledBox);
        tilFilled.setHint("Your name");
        tilFilled.setStartIconDrawable(R.drawable.ic_person);
        tilFilled.setCounterEnabled(true);
        tilFilled.setCounterMaxLength(40);
        tilFilled.setLayoutParams(withTopMargin(px(4)));
        TextInputEditText etFilled = new TextInputEditText(this);
        tilFilled.addView(etFilled);
        inner.addView(tilFilled);

        // Outlined
        inner.addView(subLabel("Outlined (with helper + clear icon)"));
        TextInputLayout tilOutlined = new TextInputLayout(this, null,
                com.google.android.material.R.style.Widget_Material3_TextInputLayout_OutlinedBox);
        tilOutlined.setHint("Search");
        tilOutlined.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        tilOutlined.setHelperText("Type to search components");
        tilOutlined.setLayoutParams(withTopMargin(px(4)));
        tilOutlined.addView(new TextInputEditText(this));
        inner.addView(tilOutlined);

        // Dense Filled
        inner.addView(subLabel("Dense Filled"));
        TextInputLayout tilDense = new TextInputLayout(this, null,
                com.google.android.material.R.style.Widget_Material3_TextInputLayout_FilledBox_Dense);
        tilDense.setHint("Dense filled field");
        tilDense.setLayoutParams(withTopMargin(px(4)));
        tilDense.addView(new TextInputEditText(this));
        inner.addView(tilDense);

        // Password
        inner.addView(subLabel("Password (toggle visibility)"));
        TextInputLayout tilPw = new TextInputLayout(this, null,
                com.google.android.material.R.style.Widget_Material3_TextInputLayout_OutlinedBox);
        tilPw.setHint("Password");
        tilPw.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        tilPw.setStartIconDrawable(R.drawable.ic_lock);
        tilPw.setLayoutParams(withTopMargin(px(4)));
        TextInputEditText etPw = new TextInputEditText(this);
        etPw.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tilPw.addView(etPw);
        inner.addView(tilPw);

        // Error state
        inner.addView(subLabel("Error state"));
        TextInputLayout tilError = new TextInputLayout(this, null,
                com.google.android.material.R.style.Widget_Material3_TextInputLayout_OutlinedBox);
        tilError.setHint("Email");
        tilError.setErrorEnabled(true);
        tilError.setError("Invalid email format");
        tilError.setLayoutParams(withTopMargin(px(4)));
        TextInputEditText etError = new TextInputEditText(this);
        etError.setText("user@");
        tilError.addView(etError);
        inner.addView(tilError);

        // Exposed Dropdown
        inner.addView(subLabel("Exposed Dropdown Menu"));
        TextInputLayout tilDd = new TextInputLayout(this, null,
                com.google.android.material.R.style.Widget_Material3_TextInputLayout_OutlinedBox_ExposedDropdownMenu);
        tilDd.setHint("Select category");
        tilDd.setLayoutParams(withTopMargin(px(4)));
        AutoCompleteTextView actv = new AutoCompleteTextView(this);
        actv.setInputType(android.text.InputType.TYPE_NULL);
        String[] ddItems = {"Design Systems","Android Dev","Material 3","Typography","Motion"};
        actv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, ddItems));
        tilDd.addView(actv);
        inner.addView(tilDd);

        inner.addView(snippetBtn("TextInputLayout — All variants",
            "<!-- ① Filled with leading icon + counter -->\n" +
            "<com.google.android.material.textfield.TextInputLayout\n" +
            "    style=\"@style/Widget.Material3.TextInputLayout.FilledBox\"\n" +
            "    android:hint=\"Label\"\n" +
            "    app:startIconDrawable=\"@drawable/ic_person\"\n" +
            "    app:counterEnabled=\"true\"\n" +
            "    app:counterMaxLength=\"40\">\n" +
            "    <com.google.android.material.textfield.TextInputEditText\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:maxLength=\"40\" />\n" +
            "</com.google.android.material.textfield.TextInputLayout>\n\n" +
            "<!-- ② Outlined with clear icon + helper -->\n" +
            "<com.google.android.material.textfield.TextInputLayout\n" +
            "    style=\"@style/Widget.Material3.TextInputLayout.OutlinedBox\"\n" +
            "    android:hint=\"Label\"\n" +
            "    app:endIconMode=\"clear_text\"\n" +
            "    app:helperText=\"Helper text\" />\n\n" +
            "<!-- ③ Dense Filled -->\n" +
            "style=\"@style/Widget.Material3.TextInputLayout.FilledBox.Dense\"\n\n" +
            "<!-- ④ Password toggle -->\n" +
            "app:endIconMode=\"password_toggle\"\n" +
            "InputType: textPassword\n\n" +
            "<!-- ⑤ Error state -->\n" +
            "app:errorEnabled=\"true\"\n" +
            "// Java: til.setError(\"msg\");  til.setError(null);\n\n" +
            "<!-- ⑥ Exposed Dropdown -->\n" +
            "style=\"@style/Widget.Material3.TextInputLayout.OutlinedBox.ExposedDropdownMenu\"\n" +
            "<AutoCompleteTextView android:inputType=\"none\" />\n" +
            "// actv.setAdapter(new ArrayAdapter<>(ctx, simple_dropdown_item_1line, items));"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 6. CHIPS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildChips() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        ChipGroup group = new ChipGroup(this);
        group.setLayoutParams(withTopMargin(px(4)));

        // Assist
        Chip assist = new Chip(this, null,
                com.google.android.material.R.style.Widget_Material3_Chip_Assist);
        assist.setText("📍 Assist");
        assist.setOnClickListener(v -> showSnippet("Chip — All Types",
            "<!-- All chips live inside a ChipGroup -->\n" +
            "<com.google.android.material.chip.ChipGroup\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\">\n\n" +
            "    <!-- ① Assist chip -->\n" +
            "    <com.google.android.material.chip.Chip\n" +
            "        style=\"@style/Widget.Material3.Chip.Assist\"\n" +
            "        android:text=\"Location\"\n" +
            "        app:chipIcon=\"@drawable/ic_info\" />\n\n" +
            "    <!-- ② Filter chip (checkable) -->\n" +
            "    <com.google.android.material.chip.Chip\n" +
            "        style=\"@style/Widget.Material3.Chip.Filter\"\n" +
            "        android:checkable=\"true\"\n" +
            "        android:text=\"Design\" />\n\n" +
            "    <!-- ③ Input chip (closeable tag) -->\n" +
            "    <com.google.android.material.chip.Chip\n" +
            "        style=\"@style/Widget.Material3.Chip.Input\"\n" +
            "        android:text=\"Kotlin\"\n" +
            "        app:closeIconEnabled=\"true\" />\n" +
            "    // chip.setOnCloseIconClickListener { chipGroup.removeView(chip) }\n\n" +
            "    <!-- ④ Suggestion chip -->\n" +
            "    <com.google.android.material.chip.Chip\n" +
            "        style=\"@style/Widget.Material3.Chip.Suggestion\"\n" +
            "        android:text=\"Sure!\" />\n\n" +
            "</com.google.android.material.chip.ChipGroup>\n\n" +
            "// ChipGroup single-select:\n" +
            "chipGroup.isSingleSelection = true\n" +
            "// Multi-select: isSingleSelection = false (default)"
        ));
        group.addView(assist);

        // Filter
        for (String s : new String[]{"Filter","Design","Motion"}) {
            Chip f = new Chip(this, null,
                    com.google.android.material.R.style.Widget_Material3_Chip_Filter);
            f.setText(s);
            f.setCheckable(true);
            f.setOnCheckedChangeListener((c, checked) ->
                Toast.makeText(this, s + ": " + (checked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show());
            group.addView(f);
        }

        // Input (closeable)
        Chip input = new Chip(this, null,
                com.google.android.material.R.style.Widget_Material3_Chip_Input);
        input.setText("Android");
        input.setCloseIconVisible(true);
        input.setOnCloseIconClickListener(v -> group.removeView(input));
        group.addView(input);

        // Suggestion
        Chip sug = new Chip(this, null,
                com.google.android.material.R.style.Widget_Material3_Chip_Suggestion);
        sug.setText("Sure!");
        sug.setOnClickListener(v -> Toast.makeText(this, "Sure!", Toast.LENGTH_SHORT).show());
        group.addView(sug);

        inner.addView(group);
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 7. SELECTION CONTROLS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildSelectionControls() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        // Checkboxes with parent/child
        inner.addView(subLabel("Checkboxes (parent/child indeterminate)"));
        MaterialCheckBox parent = new MaterialCheckBox(this);
        parent.setText("Select All");
        MaterialCheckBox child1 = new MaterialCheckBox(this);
        child1.setText("Option A");
        child1.setChecked(true);
        LinearLayout.LayoutParams c1lp = new LinearLayout.LayoutParams(-2, -2);
        c1lp.leftMargin = px(24);
        child1.setLayoutParams(c1lp);
        MaterialCheckBox child2 = new MaterialCheckBox(this);
        child2.setText("Option B");
        LinearLayout.LayoutParams c2lp = new LinearLayout.LayoutParams(-2, -2);
        c2lp.leftMargin = px(24);
        child2.setLayoutParams(c2lp);
        MaterialCheckBox child3 = new MaterialCheckBox(this);
        child3.setText("Option C (disabled)");
        child3.setEnabled(false);
        LinearLayout.LayoutParams c3lp = new LinearLayout.LayoutParams(-2, -2);
        c3lp.leftMargin = px(24);
        child3.setLayoutParams(c3lp);

        Runnable syncParent = () -> {
            boolean a = child1.isChecked(), b = child2.isChecked();
            parent.setChecked(a && b);
            parent.setButtonDrawable(
                (a && !b) || (!a && b)
                    ? null
                    : null);
        };
        parent.setOnCheckedChangeListener((v, checked) -> {
            child1.setChecked(checked);
            child2.setChecked(checked);
        });
        child1.setOnCheckedChangeListener((v, c) -> syncParent.run());
        child2.setOnCheckedChangeListener((v, c) -> syncParent.run());
        inner.addView(parent);
        inner.addView(child1);
        inner.addView(child2);
        inner.addView(child3);

        inner.addView(snippetBtn("MaterialCheckBox",
            "<!-- In XML -->\n" +
            "<com.google.android.material.checkbox.MaterialCheckBox\n" +
            "    android:id=\"@+id/checkbox\"\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:checked=\"true\"\n" +
            "    android:text=\"Option A\" />\n\n" +
            "// Listener:\n" +
            "checkbox.setOnCheckedChangeListener { _, isChecked ->\n" +
            "    // use isChecked\n" +
            "}\n\n" +
            "// Programmatic parent/child pattern:\n" +
            "parentCheckbox.setOnCheckedChangeListener { _, checked ->\n" +
            "    child1.isChecked = checked\n" +
            "    child2.isChecked = checked\n" +
            "}"
        ));

        // Radio Buttons
        inner.addView(subLabel("Radio Buttons"));
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        for (String s : new String[]{"Option A", "Option B", "Option C"}) {
            MaterialRadioButton rb = new MaterialRadioButton(this);
            rb.setText(s);
            radioGroup.addView(rb);
        }
        radioGroup.check(((MaterialRadioButton)radioGroup.getChildAt(0)).getId());
        radioGroup.setOnCheckedChangeListener((g, id) -> {
            MaterialRadioButton rb = g.findViewById(id);
            if (rb != null) Toast.makeText(this, rb.getText(), Toast.LENGTH_SHORT).show();
        });
        inner.addView(radioGroup);

        inner.addView(snippetBtn("MaterialRadioButton",
            "<!-- RadioGroup wraps MaterialRadioButtons -->\n" +
            "<RadioGroup\n" +
            "    android:id=\"@+id/radio_group\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:orientation=\"horizontal\">\n\n" +
            "    <com.google.android.material.radiobutton.MaterialRadioButton\n" +
            "        android:id=\"@+id/radio_a\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:checked=\"true\"\n" +
            "        android:text=\"Option A\" />\n" +
            "</RadioGroup>\n\n" +
            "// Listener:\n" +
            "radioGroup.setOnCheckedChangeListener { _, checkedId ->\n" +
            "    when (checkedId) {\n" +
            "        R.id.radio_a -> { /* Option A */ }\n" +
            "    }\n" +
            "}"
        ));

        // Switches
        inner.addView(subLabel("Switches"));
        for (String s : new String[]{"Enable notifications","Dark mode","Auto-sync"}) {
            LinearLayout swRow = new LinearLayout(this);
            swRow.setOrientation(LinearLayout.HORIZONTAL);
            swRow.setGravity(Gravity.CENTER_VERTICAL);
            swRow.setLayoutParams(withTopMargin(px(4)));
            TextView swTv = new TextView(this);
            swTv.setText(s);
            swTv.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
            SwitchMaterial sw = new SwitchMaterial(this);
            sw.setChecked(s.equals("Enable notifications"));
            swRow.addView(swTv);
            swRow.addView(sw);
            inner.addView(swRow);
        }

        inner.addView(snippetBtn("SwitchMaterial",
            "<!-- Switch row (label + switch) -->\n" +
            "<LinearLayout\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:gravity=\"center_vertical\"\n" +
            "    android:orientation=\"horizontal\">\n\n" +
            "    <TextView\n" +
            "        android:layout_width=\"0dp\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_weight=\"1\"\n" +
            "        android:text=\"Enable notifications\" />\n\n" +
            "    <com.google.android.material.switchmaterial.SwitchMaterial\n" +
            "        android:id=\"@+id/switch_notif\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:checked=\"true\" />\n" +
            "</LinearLayout>\n\n" +
            "// Listener:\n" +
            "switchNotif.setOnCheckedChangeListener { _, isChecked ->\n" +
            "    // use isChecked\n" +
            "}"
        ));

        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 8. SLIDERS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildSliders() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        // Continuous
        inner.addView(subLabel("Continuous (0–100)"));
        TextView contVal = monoLabel("Value: 40");
        inner.addView(contVal);
        Slider continuous = new Slider(this);
        continuous.setValueFrom(0f);
        continuous.setValueTo(100f);
        continuous.setValue(40f);
        continuous.setLayoutParams(withTopMargin(px(4)));
        continuous.addOnChangeListener((s, v, u) -> contVal.setText("Value: " + (int)v));
        inner.addView(continuous);

        // Discrete
        inner.addView(subLabel("Discrete — stepSize=1 (rating 0–5)"));
        TextView discVal = monoLabel("Rating: 3 / 5");
        inner.addView(discVal);
        Slider discrete = new Slider(this);
        discrete.setValueFrom(0f);
        discrete.setValueTo(5f);
        discrete.setValue(3f);
        discrete.setStepSize(1f);
        discrete.setLayoutParams(withTopMargin(px(4)));
        discrete.addOnChangeListener((s, v, u) -> discVal.setText("Rating: " + (int)v + " / 5"));
        inner.addView(discrete);

        // Range
        inner.addView(subLabel("Range Slider (two thumbs, 20–80)"));
        TextView rangeVal = monoLabel("Range: 20 – 80");
        inner.addView(rangeVal);
        RangeSlider range = new RangeSlider(this);
        range.setValueFrom(0f);
        range.setValueTo(100f);
        range.setValues(20f, 80f);
        range.setLayoutParams(withTopMargin(px(4)));
        range.addOnChangeListener((s, v, u) -> {
            int lo = range.getValues().get(0).intValue();
            int hi = range.getValues().get(1).intValue();
            rangeVal.setText("Range: " + lo + " – " + hi);
        });
        inner.addView(range);

        inner.addView(snippetBtn("Slider & RangeSlider",
            "<!-- ① Continuous slider -->\n" +
            "<com.google.android.material.slider.Slider\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:valueFrom=\"0\"\n" +
            "    android:valueTo=\"100\"\n" +
            "    android:value=\"40\"\n" +
            "    app:labelBehavior=\"floating\" />\n\n" +
            "<!-- ② Discrete (stepped) -->\n" +
            "<com.google.android.material.slider.Slider\n" +
            "    android:valueFrom=\"0\"\n" +
            "    android:valueTo=\"5\"\n" +
            "    android:value=\"3\"\n" +
            "    app:stepSize=\"1\" />\n\n" +
            "<!-- ③ Range slider (two thumbs) -->\n" +
            "<com.google.android.material.slider.RangeSlider\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:valueFrom=\"0\"\n" +
            "    android:valueTo=\"100\"\n" +
            "    app:values=\"@array/range_slider_values\" />\n\n" +
            "<!-- res/values/arrays.xml -->\n" +
            "<array name=\"range_slider_values\">\n" +
            "    <item>20</item>\n" +
            "    <item>80</item>\n" +
            "</array>\n\n" +
            "// Listener:\n" +
            "slider.addOnChangeListener { s, value, fromUser -> }\n" +
            "rangeSlider.addOnChangeListener { s, _, _ ->\n" +
            "    val lo = s.values[0]; val hi = s.values[1]\n" +
            "}"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 9. DIVIDERS
    // ══════════════════════════════════════════════════════════════════════════
    private View buildDividers() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(subLabel("Full-width divider"));
        inner.addView(listRow("Item One"));
        inner.addView(makeDivider(0));
        inner.addView(listRow("Item Two"));
        inner.addView(makeDivider(0));
        inner.addView(listRow("Item Three"));

        inner.addView(subLabel("Inset divider (indent 72dp — aligns after icon)"));
        inner.addView(listRow("👤  Alice"));
        inner.addView(makeDivider(px(72)));
        inner.addView(listRow("👤  Bob"));
        inner.addView(makeDivider(px(72)));
        inner.addView(listRow("👤  Charlie"));

        inner.addView(subLabel("Vertical divider (inside a horizontal layout)"));
        LinearLayout vRow = new LinearLayout(this);
        vRow.setOrientation(LinearLayout.HORIZONTAL);
        vRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams vrlp = new LinearLayout.LayoutParams(-1, px(40));
        vrlp.topMargin = px(8);
        vRow.setLayoutParams(vrlp);
        for (int i = 0; i < 3; i++) {
            if (i > 0) {
                MaterialDivider vd = new MaterialDivider(this);
                vd.setLayoutParams(new LinearLayout.LayoutParams(-2, px(24)));
                vRow.addView(vd);
            }
            TextView seg = new TextView(this);
            seg.setText(i == 0 ? "Left" : i == 1 ? "Center" : "Right");
            seg.setGravity(Gravity.CENTER);
            seg.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));
            vRow.addView(seg);
        }
        inner.addView(vRow);

        inner.addView(snippetBtn("MaterialDivider",
            "<!-- Full-width horizontal -->\n" +
            "<com.google.android.material.divider.MaterialDivider\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\" />\n\n" +
            "<!-- Inset divider (list-style indent) -->\n" +
            "<com.google.android.material.divider.MaterialDivider\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    app:dividerInsetStart=\"72dp\" />\n\n" +
            "<!-- Custom color + thickness -->\n" +
            "<com.google.android.material.divider.MaterialDivider\n" +
            "    app:dividerColor=\"?attr/colorOutlineVariant\"\n" +
            "    app:dividerThickness=\"1dp\" />\n\n" +
            "<!-- Vertical divider inside horizontal LinearLayout -->\n" +
            "<com.google.android.material.divider.MaterialDivider\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"24dp\" />"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 10. SIDE SHEET
    // ══════════════════════════════════════════════════════════════════════════
    private View buildSideSheet() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();
        inner.addView(caption("Slides in from right edge — used for filters, settings, detail panels."));

        MaterialButton openBtn = new MaterialButton(this);
        openBtn.setText("Open Filter Side Sheet →");
        openBtn.setLayoutParams(withTopMargin(px(8)));
        openBtn.setOnClickListener(v -> {
            SideSheetDialog dialog = new SideSheetDialog(this);
            LinearLayout sheetContent = new LinearLayout(this);
            sheetContent.setOrientation(LinearLayout.VERTICAL);
            sheetContent.setPadding(px(24), px(24), px(24), px(24));

            TextView title = new TextView(this);
            title.setText("Filter Options");
            title.setTextSize(20f);
            title.setTypeface(title.getTypeface(), Typeface.BOLD);
            sheetContent.addView(title);

            for (String opt : new String[]{"By Date","By Category","By Author","By Rating"}) {
                MaterialCheckBox cb = new MaterialCheckBox(this);
                cb.setText(opt);
                sheetContent.addView(cb);
            }

            MaterialButton applyBtn = new MaterialButton(this);
            applyBtn.setText("Apply Filters");
            LinearLayout.LayoutParams ablp = new LinearLayout.LayoutParams(-2, -2);
            ablp.topMargin = px(24);
            applyBtn.setLayoutParams(ablp);
            applyBtn.setOnClickListener(av -> {
                dialog.dismiss();
                Snackbar.make(rootView, "Filters applied", Snackbar.LENGTH_SHORT).show();
            });
            sheetContent.addView(applyBtn);

            dialog.setContentView(sheetContent);
            dialog.show();
        });
        inner.addView(openBtn);

        inner.addView(snippetBtn("SideSheetDialog",
            "// Modal SideSheet — programmatic:\n" +
            "val dialog = SideSheetDialog(context)\n\n" +
            "val content = layoutInflater.inflate(\n" +
            "    R.layout.layout_side_sheet, null)\n" +
            "dialog.setContentView(content)\n" +
            "dialog.show()\n\n" +
            "// Persistent SideSheet in CoordinatorLayout XML:\n" +
            "<LinearLayout\n" +
            "    android:id=\"@+id/side_sheet\"\n" +
            "    android:layout_width=\"256dp\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    android:layout_gravity=\"end\"\n" +
            "    app:layout_behavior=\"\n" +
            "     com.google.android.material.sidesheet.SideSheetBehavior\">\n" +
            "</LinearLayout>\n\n" +
            "// Java:\n" +
            "SideSheetBehavior<View> behavior =\n" +
            "    SideSheetBehavior.from(sideSheet);\n" +
            "behavior.expand(); // or .hide()\n\n" +
            "// Requires material:1.8.0+"
        ));
        card.addView(inner);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 11. POPUP MENU
    // ══════════════════════════════════════════════════════════════════════════
    private View buildPopupMenu() {
        MaterialCardView card = demoCard();
        LinearLayout inner = cardInner();

        inner.addView(caption("Anchor-based popup — appears attached to the triggering view."));

        MaterialButton anchorBtn = new MaterialButton(this);
        anchorBtn.setText("⋮  Show Popup Menu");
        anchorBtn.setLayoutParams(withTopMargin(px(8)));
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

        inner.addView(subLabel("Icon button overflow pattern"));
        MaterialButton overflow = new MaterialButton(this, null,
                com.google.android.material.R.style.Widget_Material3_Button_IconButton);
        overflow.setIconResource(R.drawable.ic_more_vert);
        overflow.setIconPadding(0);
        overflow.setLayoutParams(withTopMargin(px(4)));
        overflow.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popup =
                    new androidx.appcompat.widget.PopupMenu(this, v);
            popup.getMenu().add(0, 1, 0, "Edit");
            popup.getMenu().add(0, 2, 1, "Share");
            popup.getMenu().add(0, 3, 2, "Archive");
            popup.getMenu().add(0, 4, 3, "Delete");
            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });
            popup.show();
        });
        inner.addView(overflow);

        inner.addView(snippetBtn("PopupMenu",
            "// Kotlin/Java:\n" +
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
            "// res/menu/popup_menu.xml:\n" +
            "<menu xmlns:android=\"…\">\n" +
            "    <item android:id=\"@+id/action_edit\"\n" +
            "          android:title=\"Edit\" />\n" +
            "    <item android:id=\"@+id/action_share\"\n" +
            "          android:title=\"Share\" />\n" +
            "    <item android:id=\"@+id/action_delete\"\n" +
            "          android:title=\"Delete\" />\n" +
            "</menu>"
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
                    Snackbar.make(rootView, "Snippet copied!", Snackbar.LENGTH_SHORT).show();
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

    private View centeredLabel(String text) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        ll.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(13f);
        tv.setGravity(Gravity.CENTER);
        ll.addView(tv);
        return ll;
    }

    private View listRow(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(15f);
        tv.setPadding(0, px(12), 0, px(12));
        return tv;
    }

    private MaterialDivider makeDivider(int inset) {
        MaterialDivider d = new MaterialDivider(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.leftMargin = inset;
        d.setLayoutParams(lp);
        return d;
    }

    private LinearLayout.LayoutParams withTopMargin(int margin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = margin;
        return lp;
    }

    private int px(int v) { return (int)(v * dp); }
}
