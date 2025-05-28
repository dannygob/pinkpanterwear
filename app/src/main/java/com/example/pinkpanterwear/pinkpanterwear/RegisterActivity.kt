package com.example.slickkwear

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Random

class RegisterActivity : AppCompatActivity() {
    private var registerPhone: TextInputLayout? = null
    private var registerName: TextInputLayout? = null
    private var registerPassword: TextInputLayout? = null
    private var registerConfirmPassword: TextInputLayout? = null
    private var registerButton: Button? = null
    private var loadingBar: ProgressDialog? = null

    private var userRef: FirebaseFirestore? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var loginLink: TextView? = null
    private var generatedPassword: String? = null
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userRef = FirebaseFirestore.getInstance()

        registerName = findViewById<TextInputLayout>(R.id.register_name)
        registerPhone = findViewById<TextInputLayout>(R.id.register_phone)
        registerPassword = findViewById<TextInputLayout>(R.id.register_password)
        registerConfirmPassword = findViewById<TextInputLayout>(R.id.register_confirm_password)
        registerButton = findViewById<Button>(R.id.register_button)
        loadingBar = ProgressDialog(this)

        loginLink = findViewById<TextView>(R.id.register_login_link)
        loginLink.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(
                    applicationContext,
                    LoginActivity::class.java
                )
                startActivity(intent)
            }
        )

        liveUserDataValidator()


        registerButton.setOnClickListener(
            View.OnClickListener { btnClickValidateRegistrationData() }
        )
    }

    private fun liveUserDataValidator() {
        registerName!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (TextUtils.isEmpty(charSequence)) {
                        registerName!!.error = "Name cannot be empty!"
                    } else {
                        registerName!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        registerPhone!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (TextUtils.isEmpty(charSequence)) {
                        registerPhone!!.error = "Number cannot be empty!"
                    } else if ((charSequence.toString()
                            .startsWith("0") && charSequence.toString().length == 10) || charSequence.toString().length > 9 || charSequence.toString().length < 9
                    ) {
                        registerPhone!!.error = "Number format: 705..."
                    } else {
                        registerPhone!!.error = null
                        //                            registerPhone.setEndIconDrawable(R.drawable.back_icon);
//                            registerPhone.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        registerPassword!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (TextUtils.isEmpty(charSequence)) {
                        registerPassword!!.error = "Password cannot be empty!"
                    } else if (charSequence.toString().length < 8) {
                        registerPassword!!.error = "Minimum of 8 characters required"
                    } else if (!isValidPassword(charSequence)) {
                        registerPassword!!.error = "Must contain both numbers and letters"
                    } else {
                        registerPassword!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        registerConfirmPassword!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val password = registerPassword!!.editText!!.text.toString()

                    if (TextUtils.isEmpty(charSequence)) {
                        registerConfirmPassword!!.error = "Password cannot be empty!"
                    } else if (charSequence.toString() != password) {
                        registerConfirmPassword!!.error = "Passwords do not match!"
                    } else {
                        registerConfirmPassword!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )
    }

    private fun btnClickValidateRegistrationData() {
        val name = registerName!!.editText!!.text.toString()
        val phone = registerPhone!!.editText!!.text.toString()
        val password = registerPassword!!.editText!!.text.toString()
        val confirmPassword = registerConfirmPassword!!.editText!!.text.toString()

        if (TextUtils.isEmpty(name)) {
            registerName!!.error = "Name cannot be empty!"
        } else if (TextUtils.isEmpty(phone)) {
            registerPhone!!.error = "Phone Number cannot be empty!"
        } else if (phone.startsWith("0") || phone.length > 9 || phone.length < 9) {
            registerPhone!!.error = "Phone Number format: 705..."
        } else if (TextUtils.isEmpty(password)) {
            registerPassword!!.error = "Password cannot be empty!"
        } else if (password.length < 8) {
            registerPassword!!.error = "Minimum of 8 characters required"
        } else if (!isValidPassword(password)) {
            registerPassword!!.error = "Must contain both numbers and letters"
        } else if (TextUtils.isEmpty(confirmPassword)) {
            registerConfirmPassword!!.error = "Password cannot be empty!"
        } else if (confirmPassword != password) {
            registerConfirmPassword!!.error = "Passwords do not match!"
        } else {
            loadingBar!!.setTitle("Creating Account")
            loadingBar!!.setMessage("Please wait...")
            loadingBar!!.setCanceledOnTouchOutside(false)
            loadingBar!!.show()

            registerUser(name, phone, password)

            //            checkIfUserExist();
        }
    }

    //    private void checkIfUserExist() {
    //    }
    private fun registerUser(name: String, phone: String, password: String) {
        userID = "254$phone"

        userRef!!.collection("Users").document(userID!!).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@RegisterActivity,
                        "User +$userID exist!", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveUserData(userID!!, name, password)
                }
            }
    }

    private fun saveUserData(userID: String, name: String, password: String) {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val currentDate = SimpleDateFormat("yyyy/MM/dd")
        saveCurrentDate = currentDate.format(calendar.time)
        @SuppressLint("SimpleDateFormat") val currentTime = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calendar.time)

        //        String userID = saveCurrentDate + saveCurrentTime;
//        userID = userID.replaceAll("[^\\d]", "");
//        int randomNumber = new Random().nextInt(999999) + 100000;
//        userID = userID + randomNumber;
        val rand = Random()
        val OTP = String.format("%04d", rand.nextInt(10000))

        passWordHash(password)


        val productMap = HashMap<String, Any?>()
        productMap["UseID"] = userID
        productMap["UserName"] = name
        productMap["UserPhoneNumber"] = userID
        productMap["UserPassword"] = generatedPassword
        productMap["UserOTP"] = OTP
        productMap["UserStatus"] = "active"
        productMap["UserVerified"] = "false"
        productMap["CategoryDeleted"] = "false"

        userRef!!.collection("Users").document(userID).set(productMap)
            .addOnSuccessListener {
                Thread {
                    // Send Alert
                    //                                        String baseUrl = "";
                    //                                        int partnerId = ;
                    //                                        String apiKey = "";
                    //                                        String shortCode = "";
                    //
                    //                                        SmsGateway gateway = new SmsGateway(baseUrl, partnerId, apiKey, shortCode);
                    //
                    //                                        String[] strings = {userID};
                    //                                        String user_msg = OTP + ": is your Verification Code for Slickk Wear App.";
                    //
                    //                                        try {
                    //                                            String res = gateway.sendBulkSms(user_msg, strings);
                    //                                            System.out.println(res);
                    //                                        } catch (IOException e) {
                    //                                            e.printStackTrace();
                    //                                        }
                }.start()
                loadingBar!!.dismiss()
                val intent = Intent(
                    applicationContext,
                    PhoneVerificationActivity::class.java
                )
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("userID", userID)
                startActivity(intent)
            }
            .addOnFailureListener {
                loadingBar!!.dismiss()
                Toast.makeText(applicationContext, "Error! user not created", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun passWordHash(password: String) {
        val passwordToHash = password
        generatedPassword = null
        try {
            // Create MessageDigest instance for MD5
            val md = MessageDigest.getInstance("MD5")
            //Add password bytes to digest
            md.update(passwordToHash.toByteArray())
            //Get the hash's bytes
            val bytes = md.digest()
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(((bytes[i].toInt() and 0xff) + 0x100).toString(16).substring(1))
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    companion object {
        private fun isValidPassword(charSequence: CharSequence): Boolean {
            val n = ".*[0-9].*"
            val caps_l = ".*[A-Z].*"
            val small_l = ".*[a-z].*"
            return charSequence.toString().matches(n.toRegex()) && (charSequence.toString()
                .matches(caps_l.toRegex()) || charSequence.toString().matches(small_l.toRegex()))
        }
    }
}