package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlowCard
import com.example.ui.components.NeonButton
import com.example.ui.components.SectionTitle
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContactScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Form states collected from VM
    val contactName by viewModel.contactName.collectAsState()
    val contactEmail by viewModel.contactEmail.collectAsState()
    val contactSubject by viewModel.contactSubject.collectAsState()
    val contactMsg by viewModel.contactMessageText.collectAsState()
    val isContactSubmitting by viewModel.isContactSubmitting.collectAsState()
    val contactFeedback by viewModel.contactFeedback.collectAsState()

    val quoteProjectType by viewModel.quoteProjectType.collectAsState()
    val quoteBudget by viewModel.quoteBudget.collectAsState()
    val quoteDesc by viewModel.quoteDescription.collectAsState()
    val isQuoteSubmitting by viewModel.isQuoteSubmitting.collectAsState()
    val quoteFeedback by viewModel.quoteFeedback.collectAsState()

    // Database entries collected from VM
    val quoteRequestsList by viewModel.quoteRequests.collectAsState()
    val contactMessagesList by viewModel.contactMessages.collectAsState()

    // Dropdown expanding parameters
    var subjectMenuExpanded by remember { mutableStateOf(false) }
    var projectTypeMenuExpanded by remember { mutableStateOf(false) }

    val subjects = listOf("Graphisme", "Formation", "Partenariat", "Autre")
    val projectTypes = listOf("Affiche", "Flyer", "Identité Visuelle", "Mock-up 3D", "Autre")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "CONTACT & DEVIS",
            style = MaterialTheme.typography.displayMedium,
            color = PureWhite,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("contact_header_title")
        )
        Text(
            text = "Prenez contact avec Cyrus Baudelaire TOSSOU (Mr Koffi)",
            color = NeonGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Coordinates summary row
        Text(
            text = "Nos Coordonnées",
            style = MaterialTheme.typography.titleLarge,
            color = PureWhite,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ContactInfoRow(
                icon = Icons.Default.Email,
                title = "Email Professionnel",
                value = "cyrustossou728@gmail.com",
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:cyrustossou728@gmail.com")
                    }
                    context.startActivity(intent)
                }
            )
            ContactInfoRow(
                icon = Icons.Default.Phone,
                title = "Service WhatsApp",
                value = "+229 62 32 69 66",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/22962326966"))
                    context.startActivity(intent)
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Social platforms buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SocialButton(
                text = "TikTok",
                icon = Icons.Default.MusicNote,
                modifier = Modifier.weight(1f),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@koffistepsplus"))
                    context.startActivity(intent)
                }
            )
            SocialButton(
                text = "Facebook",
                icon = Icons.Default.Group,
                modifier = Modifier.weight(1f),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/cyrusbmr.koffi"))
                    context.startActivity(intent)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Contact message card
        GlowCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("contact_message_form"),
            borderColor = NeonGreen
        ) {
            Text(
                text = "Formulaire de Contact",
                style = MaterialTheme.typography.titleLarge,
                color = PureWhite,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Inputs
            OutlinedTextField(
                value = contactName,
                onValueChange = { viewModel.setContactName(it) },
                label = { Text("Votre Nom *", color = SlateGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite,
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = BorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_input_name")
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contactEmail,
                onValueChange = { viewModel.setContactEmail(it) },
                label = { Text("Votre Adresse Email *", color = SlateGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite,
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = BorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_input_email")
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Custom Dropdown for Subject
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = contactSubject,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Objet du Message *", color = SlateGray) },
                    trailingIcon = {
                        IconButton(onClick = { subjectMenuExpanded = true }) {
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
                        .clickable { subjectMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = subjectMenuExpanded,
                    onDismissRequest = { subjectMenuExpanded = false },
                    modifier = Modifier
                        .background(CardBackground)
                        .border(1.dp, BorderColor)
                ) {
                    subjects.forEach { sub ->
                        DropdownMenuItem(
                            text = { Text(sub, color = PureWhite) },
                            onClick = {
                                viewModel.setContactSubject(sub)
                                subjectMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contactMsg,
                onValueChange = { viewModel.setContactMessageText(it) },
                label = { Text("Votre Message *", color = SlateGray) },
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite,
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = BorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_input_message")
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (contactFeedback != null) {
                Text(
                    text = contactFeedback ?: "",
                    color = if (contactFeedback!!.contains("succès")) NeonGreen else ElectricRed,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            NeonButton(
                text = if (isContactSubmitting) "Envoi en cours..." else "Envoyer le Message",
                onClick = { viewModel.submitContactForm() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_btn_submit")
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Quote request form card
        GlowCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quote_request_form"),
            borderColor = NeonGreen
        ) {
            Text(
                text = "Formulaire de Devis",
                style = MaterialTheme.typography.titleLarge,
                color = PureWhite,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Project Type
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = quoteProjectType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type de Projet *", color = SlateGray) },
                    trailingIcon = {
                        IconButton(onClick = { projectTypeMenuExpanded = true }) {
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
                        .clickable { projectTypeMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = projectTypeMenuExpanded,
                    onDismissRequest = { projectTypeMenuExpanded = false },
                    modifier = Modifier
                        .background(CardBackground)
                        .border(1.dp, BorderColor)
                ) {
                    projectTypes.forEach { pType ->
                        DropdownMenuItem(
                            text = { Text(pType, color = PureWhite) },
                            onClick = {
                                viewModel.setQuoteProjectType(pType)
                                projectTypeMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Interactive sliders for Budget estimation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Budget Estimé", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "${quoteBudget} F CFA", color = NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Slider(
                value = quoteBudget.toFloat(),
                onValueChange = { viewModel.setQuoteBudget(it.toInt()) },
                valueRange = 5000f..150000f,
                steps = 29, // steps of 5k
                colors = SliderDefaults.colors(
                    thumbColor = NeonGreen,
                    activeTrackColor = NeonGreen,
                    inactiveTrackColor = BorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quote_budget_slider")
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "5 000 F", color = SlateGray, fontSize = 11.sp)
                Text(text = "150 000 F", color = SlateGray, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = quoteDesc,
                onValueChange = { viewModel.setQuoteDescription(it) },
                label = { Text("Décrivez brièvement votre projet *", color = SlateGray) },
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite,
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = BorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quote_input_desc")
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (quoteFeedback != null) {
                Text(
                    text = quoteFeedback ?: "",
                    color = if (quoteFeedback!!.contains("transmise")) NeonGreen else ElectricRed,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            NeonButton(
                text = if (isQuoteSubmitting) "Validation en cours..." else "Demander un devis",
                onClick = { viewModel.submitQuoteForm() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quote_btn_submit")
            )
        }

        // Quote history lists - Extremely premium local persistence UX!
        if (quoteRequestsList.isNotEmpty() || contactMessagesList.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Historique d'Inquiries",
                style = MaterialTheme.typography.titleLarge,
                color = PureWhite,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                quoteRequestsList.take(5).forEach { request ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, BorderColor)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Devis: ${request.projectType}",
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = request.status, color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Budget: ${request.budget} F CFA", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = request.description, color = SlateGray, fontSize = 12.sp, maxLines = 1)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ContactInfoRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(NeonGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = SlateGray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Text(text = value, color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
            .background(CardBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = NeonGreen, modifier = Modifier.size(16.dp))
            Text(text = text, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}
