package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText

class CheckoutShippingFragment : Fragment() {

    private lateinit var fullNameEditText: TextInputEditText
    private lateinit var streetAddressEditText: TextInputEditText
    private lateinit var cityEditText: TextInputEditText
    private lateinit var zipCodeEditText: TextInputEditText
    private lateinit var countryEditText: TextInputEditText
    private lateinit var continueButton: Button
    private val checkoutViewModel: CheckoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkout_shipping, container, false)

        fullNameEditText = view.findViewById(R.id.shipping_full_name_edit_text)
        streetAddressEditText = view.findViewById(R.id.shipping_street_address_edit_text)
        cityEditText = view.findViewById(R.id.shipping_city_edit_text)
        zipCodeEditText = view.findViewById(R.id.shipping_zip_code_edit_text)
        countryEditText = view.findViewById(R.id.shipping_country_edit_text)
        continueButton = view.findViewById(R.id.shipping_continue_button)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Shipping Details"


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continueButton.setOnClickListener {
            if (validateInput()) {
                val addressMap = mapOf(
                    "fullName" to fullNameEditText.text.toString().trim(),
                    "street" to streetAddressEditText.text.toString().trim(),
                    "city" to cityEditText.text.toString().trim(),
                    "zip" to zipCodeEditText.text.toString().trim(),
                    "country" to countryEditText.text.toString().trim()
                )
                checkoutViewModel.setShippingAddress(addressMap)

                // Navigate to confirm fragment - ViewModel now holds the address
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        CheckoutConfirmFragment()
                    ) // Ensure this container ID is correct
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun validateInput(): Boolean {
        if (fullNameEditText.text.isNullOrBlank()) {
            fullNameEditText.error = "Full name is required"
            Toast.makeText(context, "Full name is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (streetAddressEditText.text.isNullOrBlank()) {
            streetAddressEditText.error = "Street address is required"
            Toast.makeText(context, "Street address is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (cityEditText.text.isNullOrBlank()) {
            cityEditText.error = "City is required"
            Toast.makeText(context, "City is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (zipCodeEditText.text.isNullOrBlank()) {
            zipCodeEditText.error = "ZIP/Postal code is required"
            Toast.makeText(context, "ZIP/Postal code is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (countryEditText.text.isNullOrBlank()) {
            countryEditText.error = "Country is required"
            Toast.makeText(context, "Country is required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}