import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.pink.R
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.ui.ViewModel.ProductDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import java.util.regex.Pattern

class UserProductDetailsActivity : AppCompatActivity() {

    private var selectedSize: String? = null
    private var selectedSizeTextView: TextView? = null
    private val viewModel: ProductDetailsViewModel by viewModels()

    private lateinit var productNameTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productDescriptionTextView: TextView
    private lateinit var productImageView: ImageView
    private lateinit var decreaseQuantityButton: Button
    private lateinit var quantityEditText: EditText
    private lateinit var increaseQuantityButton: Button
    private lateinit var addToCartButton: Button
    private lateinit var sizesLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var contentLayout: LinearLayout // ID given to the main content LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_products_details)

        // Initialize Views
        productNameTextView = findViewById(R.id.user_product_details_name)
        productPriceTextView = findViewById(R.id.user_product_details_price)
        productDescriptionTextView = findViewById(R.id.user_product_details_description)
        productImageView = findViewById(R.id.user_product_details_image)
        decreaseQuantityButton = findViewById(R.id.decrease_quantity_button)
        quantityEditText = findViewById(R.id.quantity_edit_text)
        increaseQuantityButton = findViewById(R.id.increase_quantity_button)
        addToCartButton = findViewById(R.id.add_to_cart_button)
        sizesLayout = findViewById(R.id.user_product_details_size)
        progressBar = findViewById(R.id.details_progress_bar)
        errorTextView = findViewById(R.id.details_error_text_view)
        contentLayout =
            findViewById(R.id.product_details_content_layout) // Assuming this ID was added

        val productId = intent.getStringExtra("PRODUCT_ID")
        if (productId == null) {
            Toast.makeText(this, "Product ID missing.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        viewModel.loadProductById(productId)

        quantityEditText.filters = arrayOf<InputFilter>(object : InputFilter {
            private val pattern = Pattern.compile("[0-9]*")
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                return if (source != null && !pattern.matcher(source).matches()) {
                    ""
                } else null
            }
        })
        setupQuantityButtons()
        setupAddToCartButton()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetails.collectLatest { product ->
                    if (product != null && !viewModel.isLoading.value && viewModel.error.value == null) {
                        updateProductUI(product)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.availableSizes.collectLatest { sizes ->
                    displayProductSizes(sizes) // This will display default sizes for now
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { isLoading ->
                    progressBar.isVisible = isLoading
                    contentLayout.isVisible = !isLoading // Hide content while loading
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { errorMsg ->
                    errorTextView.isVisible = errorMsg != null
                    errorTextView.text = errorMsg
                    contentLayout.isVisible = errorMsg == null // Hide content on error
                    if (errorMsg != null) {
                        Toast.makeText(this@UserProductDetailsActivity, errorMsg, Toast.LENGTH_LONG)
                            .show()
                        // Optionally finish() after a delay or if error is critical
                    }
                }
            }
        }
    }

    private fun updateProductUI(product: Product) {
        productNameTextView.text = product.name
        val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance("USD") // Or use device locale
        productPriceTextView.text = format.format(product.price)
        productDescriptionTextView.text = product.description
        Glide.with(this@UserProductDetailsActivity)
            .load(product.imageUrl)
            .placeholder(R.drawable.placeholder_image) // Ensure these exist
            .error(R.drawable.error_image)
            .into(productImageView)
        contentLayout.isVisible = true // Ensure content is visible after successful load
    }

    private fun setupQuantityButtons() {
        decreaseQuantityButton.setOnClickListener {
            var quantity = quantityEditText.text.toString().toIntOrNull() ?: 1
            if (quantity > 1) {
                quantity--
                quantityEditText.setText(quantity.toString())
            }
        }
        increaseQuantityButton.setOnClickListener {
            var quantity = quantityEditText.text.toString().toIntOrNull() ?: 1
            quantity++
            quantityEditText.setText(quantity.toString())
        }
    }

    private fun setupAddToCartButton() {
        addToCartButton.setOnClickListener {
            val quantityString = quantityEditText.text.toString()
            val quantity = quantityString.toIntOrNull()

            if (quantity == null || quantity <= 0) {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
                quantityEditText.setText("1")
                return@setOnClickListener
            }

            val currentProduct = viewModel.productDetails.value
            if (currentProduct != null) {
                if (selectedSize == null && viewModel.availableSizes.value.isNotEmpty()) {
                    Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.addToCart(
                    currentProduct,
                    selectedSize,
                    quantity
                ) // ViewModel handles cart logic
                Toast.makeText(this, "${currentProduct.name} added to cart!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "Error adding item: Product details not available.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun displayProductSizes(sizes: List<String>) {
        sizesLayout.removeAllViews()
        selectedSizeTextView = null
        selectedSize = null

        if (sizes.isEmpty()) {
            // Optionally hide the sizes section or show a "Not available" message
            // For now, it will just be empty.
            TextView(this).apply {
                text = "Sizes not specified for this product."
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(16, 8, 16, 8) }
            }
            // sizesLayout.addView(noSizeTextView) // Uncomment to show message
            return
        }

        for (size in sizes) {
            val sizeTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 8, 16, 8)
                }
                text = size
                setBackgroundResource(R.drawable.size_indicator_background)
                setPadding(16, 8, 16, 8) // Padding might be better in the drawable itself
                isClickable = true
                isFocusable = true // For accessibility
                // Text color will be handled by the theme (colorOnPrimary for selected, colorOnSurface for default)
            }

            sizeTextView.setOnClickListener { clickedView ->
                selectedSizeTextView?.isSelected = false // Deselect previous
                clickedView.isSelected = true
                selectedSize = (clickedView as TextView).text.toString()
                selectedSizeTextView = clickedView
                Toast.makeText(this, "Selected size: $selectedSize", Toast.LENGTH_SHORT).show()
            }
            sizesLayout.addView(sizeTextView)
        }
    }
}
