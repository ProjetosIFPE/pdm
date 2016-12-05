package br.edu.ifpe.tads.pdm.projeto.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.util.AndroidUtil;

/**
 * Created by Edmilson on 04/12/2016.
 */

public class AboutDialog extends DialogFragment {

    private static String DIALOG_TAG = "dialog_about";

    public static void showAbout(FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment previousFragment = fragmentManager.findFragmentByTag(DIALOG_TAG);
        if (previousFragment != null) {
            transaction.remove(previousFragment);
        }
        transaction.addToBackStack(null);
        new AboutDialog().show(transaction, DIALOG_TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();

        String versionName = AndroidUtil.getVersionName(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutBody.append(Html.fromHtml(getString(R.string.about_dialog_text, versionName), Html.FROM_HTML_MODE_COMPACT));
        } else {
            aboutBody.append(Html.fromHtml(getString(R.string.about_dialog_text, versionName)));
        }

        View dialogView = buildDialogView(aboutBody);

        return buildDialog(dialogView);
    }

    public View buildDialogView(SpannableStringBuilder dialogText) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout dialogViewGroup = (LinearLayout) inflater.inflate(R.layout.dialog_about, null);

        TextView dialogTextView = (TextView) dialogViewGroup.findViewById(R.id.about_dialog_text);
        dialogTextView.setText(dialogText);
        dialogTextView.setMovementMethod(new LinkMovementMethod());

        ImageButton dialogButton = (ImageButton) dialogViewGroup.findViewById(R.id.ic_github_link);
        dialogButton.setOnClickListener(onClickGitHubButton());

        return dialogViewGroup;
    }

    public View.OnClickListener onClickGitHubButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGitHubLink();
            }
        };
    }

    public void goToGitHubLink() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.about_github_link)));
        startActivity(browserIntent);
    }


    public Dialog buildDialog(View dialogView) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about_dialog_title)
                .setView(dialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }


}
