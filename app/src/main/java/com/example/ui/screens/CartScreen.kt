package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GlowCard
import com.example.ui.components.NeonButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.PaymentState

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()
    val paymentPhone by viewModel.paymentPhoneNumber.collectAsState()
    val paymentNet by viewModel.paymentNetwork.collectAsState()

    var showCheckoutSheet by remember { mutableStateOf(false) }
    var networkMenuExpanded by remember { mutableStateOf(false) }
    val networks = listOf("MTN Mobile Money", "Moov Money", "Orange Money")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "MON PANIER",
            style = MaterialTheme.typography.displayMedium,
            color = PureWhite,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("cart_header_title")
        )
        Text(
            text = "Gérez vos formations sélectionnées",
            color = NeonGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("cart_empty_state"),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = BorderColor,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Votre panier est vide.",
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ajoutez des formations pour commencer votre apprentissage.",
                    color = SlateGray,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                NeonButton(
                    text = "Découvrir les formations",
                    onClick = { viewModel.navigateToTab(2) } // Redirect to courses screen
                )
            }
        } else {
            // Cart contents list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("cart_items_list"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(NeonGreen.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${item.price} F CFA",
                                    color = NeonGreen,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }

                            IconButton(
                                onClick = { viewModel.removeCartItem(item) },
                                modifier = Modifier.testTag("btn_remove_cart_${item.courseId}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Supprimer",
                                    tint = ElectricRed
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary totals
            GlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = BorderColor
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total de votre panier:",
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$cartTotal F CFA",
                        color = NeonGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeonButton(
                    text = "Procéder au paiement",
                    onClick = {
                        viewModel.startCheckout()
                        showCheckoutSheet = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_proceed_payment")
                )
            }
        }
    }

    // Moneroo Secure Payment Sheet Overlay
    if (showCheckoutSheet) {
        Dialog(onDismissRequest = { if (paymentState !is PaymentState.Processing) showCheckoutSheet = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .testTag("moneroo_checkout_sheet"),
                colors = CardDefaults.cardColors(containerColor = DarkBackground),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, NeonGreen)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MONEROO PAY",
                                style = MaterialTheme.typography.titleMedium,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (paymentState !is PaymentState.Processing) {
                            IconButton(onClick = { showCheckoutSheet = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Fermer",
                                    tint = PureWhite
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CardBackground)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Agréé Moneroo API - ID: 01KJAEHEX03M07DR230NNG2Z3C",
                            color = SlateGray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedContent(targetState = paymentState) { state ->
                        when (state) {
                            is PaymentState.Idle, is PaymentState.Error -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Montant à Facturer: $cartTotal F CFA",
                                        color = PureWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // SELECT NETWORK dropdown
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        OutlinedTextField(
                                            value = paymentNet,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Réseau Mobile Money *", color = SlateGray) },
                                            trailingIcon = {
                                                IconButton(onClick = { networkMenuExpanded = true }) {
                                                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = PureWhite)
                                                }
                                            },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = PureWhite,
                                                unfocusedTextColor = PureWhite,
                                                focusedBorderColor = NeonGreen,
                                                unfocusedBorderColor = BorderColor
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { networkMenuExpanded = true }
                                        )
                                        DropdownMenu(
                                            expanded = networkMenuExpanded,
                                            onDismissRequest = { networkMenuExpanded = false },
                                            modifier = Modifier
                                                .background(CardBackground)
                                                .border(1.dp, BorderColor)
                                        ) {
                                            networks.forEach { net ->
                                                DropdownMenuItem(
                                                    text = { Text(net, color = PureWhite) },
                                                    onClick = {
                                                        viewModel.updatePaymentNetwork(net)
                                                        networkMenuExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // PHONE NUMBER input
                                    OutlinedTextField(
                                        value = paymentPhone,
                                        onValueChange = { viewModel.updatePaymentPhoneNumber(it) },
                                        label = { Text("Numéro Mobile Money (ex: 22962326966) *", color = SlateGray) },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = PureWhite,
                                            unfocusedTextColor = PureWhite,
                                            focusedBorderColor = NeonGreen,
                                            unfocusedBorderColor = BorderColor
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("moneroo_phone_input")
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    if (state is PaymentState.Error) {
                                        Text(
                                            text = state.message,
                                            color = ElectricRed,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(bottom = 12.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    NeonButton(
                                        text = "Payer avec Moneroo API",
                                        onClick = { viewModel.submitMonerooPayment() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("moneroo_btn_confirm")
                                    )
                                }
                            }

                            is PaymentState.Processing -> {
                                Column(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = NeonGreen)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Validation sécurisée en cours...",
                                        color = PureWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Un message de confirmation va s'afficher sur votre téléphone au numéro $paymentPhone.",
                                        color = SlateGray,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            is PaymentState.Success -> {
                                Column(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Succès",
                                        tint = NeonGreen,
                                        modifier = Modifier.size(54.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Paiement Réussi !",
                                        color = NeonGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Montant réglé: ${state.paidAmount} F CFA",
                                        color = PureWhite,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "N° Transaction: ${state.transactionId}",
                                        color = SlateGray,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    NeonButton(
                                        text = "Accéder à mes Formations",
                                        onClick = {
                                            showCheckoutSheet = false
                                            viewModel.dismissPayment()
                                            viewModel.navigateToTab(2) // Jump back to courses with unlocked active content!
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("moneroo_success_close")
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
