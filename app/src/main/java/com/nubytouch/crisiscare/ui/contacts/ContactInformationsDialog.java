package com.nubytouch.crisiscare.ui.contacts;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import timber.log.Timber;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.utils.Utils;

public class ContactInformationsDialog extends Dialog implements OnClickListener
{
    private Button       phoneButton;
    private LinearLayout actionPhone;
    private ImageButton  btDialPhone;
    private ImageButton  btSMSPhone;

    private Button       privateMobilePhoneButton;
    private LinearLayout actionPrivateMobilePhone;
    private ImageButton  btDialPrivateMobilePhone;
    private ImageButton  btSMSPrivateMobilePhone;

    private Button       mobilePhoneButton;
    private LinearLayout actionMobilePhone;
    private ImageButton  btDialMobilePhone;
    private ImageButton  btSMSMobilePhone;

    private Button       privatePhoneButton;
    private LinearLayout actionPrivatePhone;
    private ImageButton  btDialPrivatePhone;
    private ImageButton  btSMSPrivatePhone;

    private Button emailButton;
    private Button privateEmailButton;

    private TextView tvName;
    private User     contact;

    public ContactInformationsDialog(Context context, User contact)
    {
        super(context);
        setCancelable(true);

        this.contact = contact;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_infos);

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(contact.getFullName());
        tvName.setTextColor(Session.getPrimaryColor());
        phoneButton = (Button) findViewById(R.id.btPhone);
        actionPhone = (LinearLayout) findViewById(R.id.actionPhone);
        btDialPhone = (ImageButton) findViewById(R.id.dialPhone);
        btSMSPhone = (ImageButton) findViewById(R.id.smsPhone);
        if (!Utils.isNullOrEmpty(contact.getPhone()))
        {
            phoneButton.setText(context.getString(R.string.phone, contact.getPhone()));
            phoneButton.setOnClickListener(this);
            btDialPhone.setOnClickListener(this);
            btSMSPhone.setOnClickListener(this);
        }
        else
        {
            phoneButton.setVisibility(View.GONE);
        }

        privateMobilePhoneButton = (Button) findViewById(R.id.btPrivateMobilePhone);
        actionPrivateMobilePhone = (LinearLayout) findViewById(R.id.actionPrivateMobilePhone);
        btDialPrivateMobilePhone = (ImageButton) findViewById(R.id.dialPrivateMobilePhone);
        btSMSPrivateMobilePhone = (ImageButton) findViewById(R.id.smsPrivateMobilePhone);
        if (!Utils.isNullOrEmpty(contact.getPrivateMobilePhone()))
        {
            privateMobilePhoneButton.setText(context.getString(R.string.private_mobile_phone, contact
                    .getPrivateMobilePhone()));
            privateMobilePhoneButton.setOnClickListener(this);
            btDialPrivateMobilePhone.setOnClickListener(this);
            btSMSPrivateMobilePhone.setOnClickListener(this);
        }
        else
        {
            privateMobilePhoneButton.setVisibility(View.GONE);
        }

        mobilePhoneButton = (Button) findViewById(R.id.btMobile);
        actionMobilePhone = (LinearLayout) findViewById(R.id.actionMobile);
        btDialMobilePhone = (ImageButton) findViewById(R.id.dialMobile);
        btSMSMobilePhone = (ImageButton) findViewById(R.id.smsMobile);

        if (!Utils.isNullOrEmpty(contact.getMobilePhone()))
        {
            mobilePhoneButton.setText(context.getString(R.string.mobile, contact.getMobilePhone()));
            mobilePhoneButton.setOnClickListener(this);
            btDialMobilePhone.setOnClickListener(this);
            btSMSMobilePhone.setOnClickListener(this);
        }
        else
        {
            mobilePhoneButton.setVisibility(View.GONE);
        }

        privatePhoneButton = (Button) findViewById(R.id.btPrivatePhone);
        actionPrivatePhone = (LinearLayout) findViewById(R.id.actionPrivatePhone);
        btDialPrivatePhone = (ImageButton) findViewById(R.id.dialPrivatePhone);
        btSMSPrivatePhone = (ImageButton) findViewById(R.id.smsPrivatePhone);
        if (!Utils.isNullOrEmpty(contact.getPrivatePhone()))
        {
            privatePhoneButton.setText(context.getString(R.string.private_phone, contact
                    .getPrivatePhone()));
            privatePhoneButton.setOnClickListener(this);
            btDialPrivatePhone.setOnClickListener(this);
            btSMSPrivatePhone.setOnClickListener(this);
        }
        else
        {
            privatePhoneButton.setVisibility(View.GONE);
        }

        emailButton = (Button) findViewById(R.id.btMail);
        privateEmailButton = (Button) findViewById(R.id.btPrivateMail);

        if (!Utils.isNullOrEmpty(contact.getEmail()))
        {
            emailButton.setText(context.getString(R.string.email, contact.getEmail()));
            emailButton.setOnClickListener(this);
        }
        else
        {
            emailButton.setVisibility(View.GONE);
        }

        if (!Utils.isNullOrEmpty(contact.getPrivateEmail()))
        {
            privateEmailButton.setText(context.getString(R.string.private_email, contact.getPrivateEmail()));
            privateEmailButton.setOnClickListener(this);
        }
        else
        {
            privateEmailButton.setVisibility(View.GONE);
        }

        setCancelable(true);
    }

    public void setContact(User contact)
    {
        this.contact = contact;
    }

    public void onClick(View view)
    {
        if (view == phoneButton)
        {
            switchButton(actionPhone, phoneButton);
        }
        else if (view == privateMobilePhoneButton)
        {
            switchButton(actionPrivateMobilePhone, privateMobilePhoneButton);
        }
        else if (view == mobilePhoneButton)
        {
            switchButton(actionMobilePhone, mobilePhoneButton);
        }
        else if (view == privatePhoneButton)
        {
            switchButton(actionPrivatePhone, privatePhoneButton);
        }
        else
        {
            if (view == emailButton)
                openIntent(Utils.newEmailIntent(getContext(), contact.getEmail()));
            if (view == privateEmailButton)
                openIntent(Utils.newEmailIntent(getContext(), contact.getPrivateEmail()));
            else if (view == btDialPrivateMobilePhone)
                openIntent(Utils.newPhoneIntent(getContext(), contact.getPrivateMobilePhone()));
            else if (view == btDialPrivatePhone)
                openIntent(Utils.newPhoneIntent(getContext(), contact.getPrivatePhone()));
            else if (view == btDialMobilePhone)
                openIntent(Utils.newPhoneIntent(getContext(), contact.getMobilePhone()));
            else if (view == btDialPhone)
                openIntent(Utils.newPhoneIntent(getContext(), contact.getPhone()));
            else if (view == btSMSPrivateMobilePhone)
                openIntent(Utils.newSMSIntent(getContext(), contact.getPrivateMobilePhone()));
            else if (view == btSMSPrivatePhone)
                openIntent(Utils.newSMSIntent(getContext(), contact.getPrivatePhone()));
            else if (view == btSMSMobilePhone)
                openIntent(Utils.newSMSIntent(getContext(), contact.getMobilePhone()));
            else if (view == btSMSPhone)
                openIntent(Utils.newSMSIntent(getContext(), contact.getPhone()));

            dismiss();
        }
    }

    private void switchButton(ViewGroup layout, Button button)
    {
        boolean visible = layout.getVisibility() == View.VISIBLE;
        layout.setVisibility(visible ? View.GONE : View.VISIBLE);
        button.setCompoundDrawablesWithIntrinsicBounds(0,
                                                       0,
                                                       visible ? R.drawable.ic_action_next_item
                                                               : R.drawable.ic_action_expand,
                                                       0);
    }

    private void openIntent(Intent intent)
    {
        try
        {
            getContext().startActivity(intent);
        }
        catch (Exception e)
        {
            Timber.e("openIntent: ", e);
        }
    }
}
