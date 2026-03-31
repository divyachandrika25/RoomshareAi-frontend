package com.simats.roomshareairentalapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.launch
import org.json.JSONObject

class SubscriptionActivity : ComponentActivity(), PurchasesUpdatedListener, PaymentResultListener {

    private lateinit var billingClient: BillingClient
    private lateinit var sessionManager: SessionManager
    private var productDetailsState = mutableStateOf<ProductDetails?>(null)
    private var isBillingReady      = mutableStateOf(false)

    companion object {
        private const val TAG                  = "SubscriptionActivity"
        private const val SUBSCRIPTION_SKU     = "roomshare_premium_subscription"
        private const val TEST_SUBSCRIPTION_SKU = "android.test.purchased"
        private const val RAZORPAY_KEY         = "rzp_test_SWKknvSDD1VSWN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        Checkout.preload(applicationContext)
        setupBillingClient()

        setContent {
            RoomshareAIRentalAppTheme {
                SubscriptionScreen(
                    onSubscribeClick = { launchSubscriptionFlow() },
                    onSkipClick      = { finish() },
                    isReady          = isBillingReady.value,
                    productDetails   = productDetailsState.value,
                    isPremium        = sessionManager.isPremiumUser()
                )
            }
        }
    }

    // ─────────────────────────────────────────
    //  SUBSCRIPTION SCREEN
    // ─────────────────────────────────────────
    @Composable
    fun SubscriptionScreen(
        onSubscribeClick: () -> Unit,
        onSkipClick: () -> Unit,
        isReady: Boolean,
        productDetails: ProductDetails?,
        isPremium: Boolean = false
    ) {
        val priceText = if (isPremium) "₹100" else productDetails?.let {
            if (it.productType == BillingClient.ProductType.SUBS)
                it.subscriptionOfferDetails?.getOrNull(0)
                    ?.pricingPhases?.pricingPhaseList?.getOrNull(0)?.formattedPrice
            else
                it.oneTimePurchaseOfferDetails?.formattedPrice
        } ?: "₹100"

        Scaffold(containerColor = AppTheme.Slate50) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // ── Hero ──
                SubscriptionHero(isPremium = isPremium)

                // ── Content ──
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-24).dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Pricing card
                    PricingCard(
                        priceText = priceText,
                        isPremium = isPremium
                    )

                    // Benefits card
                    BenefitsCard()

                    // Plan comparison
                    PlanComparisonCard()

                    // CTA buttons
                    CtaSection(
                        isPremium        = isPremium,
                        onSubscribeClick = onSubscribeClick,
                        onSkipClick      = onSkipClick
                    )

                    // Legal note
                    Text(
                        "Subscription renews automatically. Cancel anytime from your account settings.",
                        fontSize = 11.sp,
                        color = AppTheme.Slate400,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }

    // ─────────────────────────────────────────
    //  HERO BANNER
    // ─────────────────────────────────────────
    @Composable
    private fun SubscriptionHero(isPremium: Boolean) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseAlpha by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue  = 1f,
            animationSpec = infiniteRepeatable(
                animation  = tween(1500, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.linearGradient(listOf(AppTheme.Blue800, AppTheme.Blue600, AppTheme.Indigo500))
                )
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .offset(x = (-70).dp, y = (-70).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 60.dp, y = (-40).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-20).dp, y = 40.dp)
                    .background(Color.White.copy(alpha = 0.04f), CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Pulsing icon box
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Color.White.copy(alpha = pulseAlpha * 0.18f),
                            RoundedCornerShape(24.dp)
                        )
                        .border(
                            2.dp,
                            Color.White.copy(alpha = 0.3f),
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AutoAwesome, null,
                        tint   = Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    if (isPremium) "You're Premium! ✨" else "Upgrade to Premium",
                    fontSize     = 26.sp,
                    fontWeight   = FontWeight.Black,
                    color        = Color.White,
                    textAlign    = TextAlign.Center,
                    letterSpacing = (-0.5).sp,
                    lineHeight   = 30.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    if (isPremium)
                        "Enjoy all premium benefits — your plan is active."
                    else
                        "AI searches, priority matching & exclusive hotel deals.",
                    fontSize  = 13.sp,
                    color     = Color.White.copy(alpha = 0.78f),
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )

                Spacer(Modifier.height(16.dp))

                // Status pill
                Surface(
                    color  = Color.White.copy(alpha = if (isPremium) 0.22f else 0.16f),
                    shape  = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    if (isPremium) AppTheme.Success else AppTheme.Gold,
                                    CircleShape
                                )
                        )
                        Spacer(Modifier.width(7.dp))
                        Text(
                            if (isPremium) "Plan Active" else "Limited Offer",
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White
                        )
                    }
                }
            }
        }
    }

    // ─────────────────────────────────────────
    //  PRICING CARD
    // ─────────────────────────────────────────
    @Composable
    private fun PricingCard(priceText: String, isPremium: Boolean) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    4.dp, RoundedCornerShape(22.dp),
                    ambientColor = AppTheme.Blue900.copy(alpha = 0.08f),
                    spotColor   = AppTheme.Blue700.copy(alpha = 0.10f)
                ),
            shape = RoundedCornerShape(22.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(AppTheme.Blue700, AppTheme.Indigo600))
                    )
            ) {
                // Decorative circle
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 40.dp, y = (-40).dp)
                        .background(Color.White.copy(alpha = 0.06f), CircleShape)
                )
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, null, tint = AppTheme.Gold, modifier = Modifier.size(16.dp))
                        }
                        Column {
                            Text("Premium Plan", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("Monthly subscription", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        }
                        Spacer(Modifier.weight(1f))
                        Surface(
                            color = if (isPremium) AppTheme.Success.copy(alpha = 0.9f)
                                    else Color.White.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                if (isPremium) "ACTIVE" else "POPULAR",
                                fontSize = 9.sp, fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                letterSpacing = 0.6.sp
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.White.copy(alpha = 0.12f)
                    )

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            priceText,
                            fontSize   = 34.sp,
                            fontWeight = FontWeight.Black,
                            color      = Color.White,
                            letterSpacing = (-1).sp
                        )
                        Text(
                            " / month",
                            fontSize  = 14.sp,
                            color     = Color.White.copy(alpha = 0.7f),
                            modifier  = Modifier.padding(bottom = 4.dp)
                        )
                        Spacer(Modifier.weight(1f))
                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "SAVE 40%",
                                fontSize = 10.sp, fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            )
                        }
                    }
                    Text(
                        "Billed monthly · Cancel anytime",
                        fontSize = 11.sp,
                        color    = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }

    // ─────────────────────────────────────────
    //  BENEFITS CARD
    // ─────────────────────────────────────────
    @Composable
    private fun BenefitsCard() {
        data class Benefit(val icon: ImageVector, val color: Color, val title: String, val subtitle: String)
        val benefits = listOf(
            Benefit(Icons.Default.AutoAwesome,     AppTheme.Indigo500, "Unlimited AI Searches",        "Real-time results, no daily caps"),
            Benefit(Icons.Default.People,          AppTheme.Blue600,   "Advanced Compatibility",        "AI-powered deep lifestyle matching"),
            Benefit(Icons.Default.Business,        AppTheme.Blue500,   "Exclusive Hotel Deals",         "Partner discounts up to 30% off"),
            Benefit(Icons.Default.Block,           AppTheme.Success,   "Ad-free Experience",            "Clean, distraction-free interface"),
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, RoundedCornerShape(22.dp),
                    ambientColor = AppTheme.Blue900.copy(alpha = 0.06f),
                    spotColor   = AppTheme.Blue700.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(22.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AppTheme.Blue600, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.WorkspacePremium, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("What You Get", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
                        Text("Everything included in Premium", fontSize = 11.sp, color = AppTheme.Slate400)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = AppTheme.Slate100)

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    benefits.forEach { (icon, color, title, subtitle) ->
                        BenefitRow(icon = icon, color = color, title = title, subtitle = subtitle)
                    }
                }
            }
        }
    }

    @Composable
    private fun BenefitRow(icon: ImageVector, color: Color, title: String, subtitle: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.10f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
                Text(subtitle, fontSize = 11.sp, color = AppTheme.Slate400, fontWeight = FontWeight.Medium)
            }
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(AppTheme.Success.copy(alpha = 0.12f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = AppTheme.Success, modifier = Modifier.size(13.dp))
            }
        }
    }

    // ─────────────────────────────────────────
    //  PLAN COMPARISON CARD
    // ─────────────────────────────────────────
    @Composable
    private fun PlanComparisonCard() {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, RoundedCornerShape(22.dp),
                    ambientColor = AppTheme.Blue900.copy(alpha = 0.06f),
                    spotColor   = AppTheme.Blue700.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(22.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AppTheme.Indigo500, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CompareArrows, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Free vs Premium", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
                        Text("See what you're missing", fontSize = 11.sp, color = AppTheme.Slate400)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = AppTheme.Slate100)

                // Column labels
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(1f))
                    Text("Free", fontSize = 10.sp, fontWeight = FontWeight.Black,
                         color = AppTheme.Slate400, modifier = Modifier.width(56.dp), textAlign = TextAlign.Center)
                    Surface(color = AppTheme.Blue600, shape = RoundedCornerShape(8.dp)) {
                        Text("Premium", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White,
                             modifier = Modifier
                                 .width(72.dp)
                                 .padding(vertical = 4.dp),
                             textAlign = TextAlign.Center)
                    }
                }

                Spacer(Modifier.height(12.dp))

                val rows = listOf(
                    Triple("AI searches / day",  "5",     "∞"),
                    Triple("Compatibility score", "Basic", "Advanced"),
                    Triple("Hotel deals",         "❌",    "✓"),
                    Triple("Ads",                 "Yes",   "None"),
                    Triple("Priority matching",   "❌",    "✓"),
                )
                rows.forEachIndexed { i, (feature, free, premium) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (i % 2 == 0) AppTheme.Slate50 else Color.White,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(feature, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                             color = AppTheme.Slate700, modifier = Modifier.weight(1f))
                        Text(free, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                             color = AppTheme.Slate400, modifier = Modifier.width(56.dp),
                             textAlign = TextAlign.Center)
                        Text(premium, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                             color = AppTheme.Blue600, modifier = Modifier.width(72.dp),
                             textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }

    // ─────────────────────────────────────────
    //  CTA SECTION
    // ─────────────────────────────────────────
    @Composable
    private fun CtaSection(
        isPremium: Boolean,
        onSubscribeClick: () -> Unit,
        onSkipClick: () -> Unit
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Primary CTA
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .then(
                        if (!isPremium) Modifier.clickable { onSubscribeClick() }
                        else Modifier
                    ),
                shape = RoundedCornerShape(18.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isPremium)
                                Brush.horizontalGradient(listOf(AppTheme.Success, AppTheme.Success))
                            else
                                Brush.horizontalGradient(listOf(AppTheme.Blue700, AppTheme.Blue600, AppTheme.Indigo500))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isPremium) Icons.Default.CheckCircle else Icons.Default.AutoAwesome,
                            null, tint = Color.White, modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            if (isPremium) "Plan Active" else "Subscribe Now",
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = Color.White
                        )
                        if (!isPremium) {
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
                                 tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Skip
            if (!isPremium) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSkipClick() },
                    shape  = RoundedCornerShape(16.dp),
                    color  = Color.White,
                    border = BorderStroke(1.5.dp, AppTheme.Slate200)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Maybe Later", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppTheme.Slate400)
                    }
                }
            }

            // Trust signals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    Pair(Icons.Default.Lock,   "Secure payment"),
                    Pair(Icons.Default.Cancel, "Cancel anytime"),
                    Pair(Icons.Default.Shield, "100% refund"),
                ).forEach { (icon, label) ->
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(icon, null, tint = AppTheme.Slate400, modifier = Modifier.size(11.dp))
                        Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.Slate400)
                    }
                }
            }
        }
    }

    // ─────────────────────────────────────────
    //  BILLING & PAYMENT LOGIC (unchanged)
    // ─────────────────────────────────────────
    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isBillingReady.value = true
                    querySubscriptionDetails()
                }
            }
            override fun onBillingServiceDisconnected() { isBillingReady.value = false }
        })
    }

    private fun querySubscriptionDetails() {
        querySpecificProduct(SUBSCRIPTION_SKU, BillingClient.ProductType.SUBS) { success ->
            if (!success) querySpecificProduct(TEST_SUBSCRIPTION_SKU, BillingClient.ProductType.INAPP) { _ -> }
        }
    }

    private fun querySpecificProduct(productId: String, productType: String, callback: (Boolean) -> Unit) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId).setProductType(productType).build()
        )
        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        ) { result, list ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && list.isNotEmpty()) {
                productDetailsState.value = list[0]; callback(true)
            } else { callback(false) }
        }
    }

    private fun launchSubscriptionFlow() {
        val details = productDetailsState.value ?: run { startRazorpayPayment(); return }
        val productDetailsParamsList = if (details.productType == BillingClient.ProductType.SUBS) {
            val offerToken = details.subscriptionOfferDetails?.getOrNull(0)?.offerToken
                ?: run { startRazorpayPayment(); return }
            listOf(BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details).setOfferToken(offerToken).build())
        } else {
            listOf(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(details).build())
        }
        billingClient.launchBillingFlow(this,
            BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build())
    }

    private fun startRazorpayPayment() {
        val checkout = Checkout()
        checkout.setKeyID(RAZORPAY_KEY)
        try {
            val options = JSONObject().apply {
                put("name",          "RoomShare AI")
                put("description",   "Premium Membership")
                put("theme.color",   "#2563EB")
                put("currency",      "INR")
                put("amount",        "10000")
                put("prefill", JSONObject().apply {
                    put("email", sessionManager.fetchUserEmail() ?: "user@roomshare.ai")
                })
            }
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) { onSubscriptionSuccess() }
    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null)
            purchases.forEach { handlePurchase(it) }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                billingClient.acknowledgePurchase(
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                ) { if (it.responseCode == BillingClient.BillingResponseCode.OK) onSubscriptionSuccess() }
            } else onSubscriptionSuccess()
        }
    }

    private fun onSubscriptionSuccess() {
        Toast.makeText(this, "Welcome to Premium! 🎉", Toast.LENGTH_LONG).show()
        sessionManager.setPremiumUser(true)
        sessionManager.fetchUserEmail()?.let { email ->
            lifecycleScope.launch {
                try { RetrofitClient.instance.updateSubscription(SubscriptionUpdateRequest(email, true)) }
                catch (_: Exception) {}
            }
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::billingClient.isInitialized) billingClient.endConnection()
    }
}