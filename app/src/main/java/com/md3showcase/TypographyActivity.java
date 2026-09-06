package com.md3showcase;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

/**
 * TypographyActivity
 *
 * Demonstrates the complete M3 type scale — all 15 roles across 5 categories:
 *   Display  (Large / Medium / Small)
 *   Headline (Large / Medium / Small)
 *   Title    (Large / Medium / Small)
 *   Body     (Large / Medium / Small)
 *   Label    (Large / Medium / Small)
 *
 * Every role shows:
 *   • Live rendering at correct size/weight/tracking
 *   • Tap → snippet dialog with XML textAppearance attribute
 *
 * XML snippet pattern:
 *   <TextView
 *       android:textAppearance="?attr/textAppearanceDisplayLarge" />
 *
 * Kotlin/Java:
 *   textView.setTextAppearance(R.style.TextAppearance_Material3_DisplayLarge);
 */
public class TypographyActivity extends AppCompatActivity {

    private View rootView;

    // All 15 M3 type roles: [label, attrName, styleRes]
    private static final Object[][] TYPE_SCALE = {
        // Display
        {"Display Large",   "?attr/textAppearanceDisplayLarge",  com.google.android.material.R.style.TextAppearance_Material3_DisplayLarge},
        {"Display Medium",  "?attr/textAppearanceDisplayMedium", com.google.android.material.R.style.TextAppearance_Material3_DisplayMedium},
        {"Display Small",   "?attr/textAppearanceDisplaySmall",  com.google.android.material.R.style.TextAppearance_Material3_DisplaySmall},
        // Headline
        {"Headline Large",  "?attr/textAppearanceHeadlineLarge",  com.google.android.material.R.style.TextAppearance_Material3_HeadlineLarge},
        {"Headline Medium", "?attr/textAppearanceHeadlineMedium", com.google.android.material.R.style.TextAppearance_Material3_HeadlineMedium},
        {"Headline Small",  "?attr/textAppearanceHeadlineSmall",  com.google.android.material.R.style.TextAppearance_Material3_HeadlineSmall},
        // Title
        {"Title Large",     "?attr/textAppearanceTitleLarge",   com.google.android.material.R.style.TextAppearance_Material3_TitleLarge},
        {"Title Medium",    "?attr/textAppearanceTitleMedium",  com.google.android.material.R.style.TextAppearance_Material3_TitleMedium},
        {"Title Small",     "?attr/textAppearanceTitleSmall",   com.google.android.material.R.style.TextAppearance_Material3_TitleSmall},
        // Body
        {"Body Large",      "?attr/textAppearanceBodyLarge",   com.google.android.material.R.style.TextAppearance_Material3_BodyLarge},
        {"Body Medium",     "?attr/textAppearanceBodyMedium",  com.google.android.material.R.style.TextAppearance_Material3_BodyMedium},
        {"Body Small",      "?attr/textAppearanceBodySmall",   com.google.android.material.R.style.TextAppearance_Material3_BodySmall},
        // Label
        {"Label Large",     "?attr/textAppearanceLabelLarge",  com.google.android.material.R.style.TextAppearance_Material3_LabelLarge},
        {"Label Medium",    "?attr/textAppearanceLabelMedium", com.google.android.material.R.style.TextAppearance_Material3_LabelMedium},
        {"Label Small",     "?attr/textAppearanceLabelSmall",  com.google.android.material.R.style.TextAppearance_Material3_LabelSmall},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Build layout programmatically
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView = rootLayout;

        MaterialToolbar toolbar = new MaterialToolbar(this);
        toolbar.setTitle("Typography Scale");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (56 * getResources().getDisplayMetrics().density)));
        rootLayout.addView(toolbar);

        NestedScrollView scroll = new NestedScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        scroll.setClipToPadding(false);
        int pad = dp(16);
        scroll.setPadding(pad, pad, pad, dp(88));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        // Section header helper
        String[] categories = {"Display", "Headline", "Title", "Body", "Label"};
        int roleIndex = 0;
        for (String cat : categories) {
            content.addView(sectionLabel(cat.toUpperCase()));
            MaterialCardView card = demoCard();
            LinearLayout inner = cardInner();
            for (int i = 0; i < 3; i++) {
                Object[] role = TYPE_SCALE[roleIndex];
                String roleName  = (String) role[0];
                String attrName  = (String) role[1];
                int    styleRes  = (int)    role[2];
                inner.addView(typeRow(roleName, attrName, styleRes));
                roleIndex++;
            }
            card.addView(inner);
            content.addView(card);
        }

        // XML reference card
        content.addView(sectionLabel("XML REFERENCE"));
        MaterialCardView refCard = demoCard();
        LinearLayout refInner = cardInner();
        refInner.addView(caption(
            "Use in any View or Widget:\n\n" +
            "  android:textAppearance=\"?attr/textAppearanceDisplayLarge\"\n" +
            "  android:textAppearance=\"?attr/textAppearanceHeadlineMedium\"\n" +
            "  android:textAppearance=\"?attr/textAppearanceTitleSmall\"\n" +
            "  android:textAppearance=\"?attr/textAppearanceBodyLarge\"\n" +
            "  android:textAppearance=\"?attr/textAppearanceLabelMedium\"\n\n" +
            "Java/Kotlin:\n" +
            "  tv.setTextAppearance(com.google.android.material.R.style\n" +
            "      .TextAppearance_Material3_BodyLarge);\n\n" +
            "M3 tokens follow the pattern:\n" +
            "  TextAppearance.Material3.<Scale><Size>"
        ));
        refCard.addView(refInner);
        content.addView(refCard);

        scroll.addView(content);
        rootLayout.addView(scroll);
        setContentView(rootLayout);
    }

    private View typeRow(String label, String attrName, int styleRes) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(12);
        row.setLayoutParams(lp);
        row.setClickable(true);
        row.setFocusable(true);
        row.setBackground(obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground})
                .getDrawable(0));

        TextView tv = new TextView(this);
        tv.setText(label);
        tv.setTextAppearance(styleRes);
        tv.setPadding(0, dp(2), 0, dp(2));
        row.addView(tv);

        TextView attr = new TextView(this);
        attr.setText(attrName);
        attr.setTextSize(10f);
        attr.setAlpha(0.5f);
        row.addView(attr);

        row.setOnClickListener(v -> showSnippet("Typography — " + label,
            "<!-- " + label + " -->\n" +
            "<TextView\n" +
            "    android:layout_width=\"wrap_content\"\n" +
            "    android:layout_height=\"wrap_content\"\n" +
            "    android:text=\"" + label + "\"\n" +
            "    android:textAppearance=\"" + attrName + "\" />\n\n" +
            "// Java:\n" +
            "textView.setTextAppearance(\n" +
            "    com.google.android.material.R.style\n" +
            "        .TextAppearance_Material3_" + label.replace(" ", "") + ");"
        ));
        return row;
    }

    private void showSnippet(String title, String code) {
        ScrollView sv = new ScrollView(this);
        TextView tv = new TextView(this);
        int p = dp(16);
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
                    cm.setPrimaryClip(android.content.ClipData.newPlainText("snippet", code));
                    Snackbar.make(rootView, "Copied!", Snackbar.LENGTH_SHORT).show();
                })
                .show();
    }

    private TextView sectionLabel(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(11f);
        tv.setAlpha(0.6f);
        tv.setLetterSpacing(0.1f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(16);
        lp.bottomMargin = dp(8);
        tv.setLayoutParams(lp);
        return tv;
    }

    private TextView caption(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(12f);
        tv.setTypeface(android.graphics.Typeface.MONOSPACE);
        tv.setPadding(0, 0, 0, dp(4));
        return tv;
    }

    private MaterialCardView demoCard() {
        MaterialCardView card = new MaterialCardView(this);
        card.setRadius(dp(12));
        card.setCardElevation(0f);
        card.setStrokeWidth(dp(1));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(12);
        card.setLayoutParams(lp);
        return card;
    }

    private LinearLayout cardInner() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        int p = dp(16);
        ll.setPadding(p, p, p, p);
        return ll;
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}
