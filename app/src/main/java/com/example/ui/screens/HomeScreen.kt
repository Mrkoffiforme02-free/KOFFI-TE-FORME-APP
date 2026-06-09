package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlowCard
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonSecondaryButton
import com.example.ui.components.SectionTitle
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Project

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Space under top bar
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Avatar with Neon Borders
        Box(
            modifier = Modifier
                .size(120.dp)
                .testTag("home_profile_avatar"),
            contentAlignment = Alignment.Center
        ) {
            // Neon Glowing Halo Ring
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonGreen.copy(alpha = 0.4f), Color.Transparent),
                        center = center,
                        radius = size.minDimension / 2
                    )
                )
            }
            // Innermost circle with border
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .border(2.dp, NeonGreen, CircleShape)
                    .background(CardBackground),
                contentAlignment = Alignment.Center
            ) {
                // Initial Letter Logo or Styled design shape
                Text(
                    text = "MK",
                    color = NeonGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Brand Logo
        Text(
            text = "MR KOFFI",
            style = MaterialTheme.typography.displayLarge,
            color = PureWhite,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("home_logo_title")
        )

        Text(
            text = "GRAPHISTE PROFESSIONNEL",
            color = NeonGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Professional Short Bio
        Text(
            text = "Je suis un graphiste mobile passionné qui transforme les idées en visuels créatifs et percutants. Avec mon smartphone comme atelier, je crée des designs modernes, accessibles et uniques qui captivent et marquent les esprits.",
            color = SlateGray,
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Direct Core CTAs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeonButton(
                text = "Voir Formations",
                onClick = { viewModel.navigateToTab(2) }, // Index 2 is Formations
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_btn_courses")
            )
            NeonSecondaryButton(
                text = "Me Contacter",
                onClick = { viewModel.navigateToTab(4) }, // Index 4 is Profile/Contact
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_btn_contact")
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Specialties section
        SectionTitle(title = "Mes Spécialités", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))

        SpecialtyRow(
            title = "Graphisme Mobile",
            desc = "Création d'affiches, logos et visuels de haute qualité entièrement optimisés et édités sur smartphone (Pixellab).",
            icon = Icons.Default.PhoneAndroid
        )
        Spacer(modifier = Modifier.height(12.dp))
        SpecialtyRow(
            title = "Design Digital",
            desc = "Social media kits complets, maquettes d'applications modernes et bannières publicitaires digitales engageantes.",
            icon = Icons.Default.Brush
        )
        Spacer(modifier = Modifier.height(12.dp))
        SpecialtyRow(
            title = "Visuels Impactants",
            desc = "Designs mémorables avec un excellent équilibre des couleurs, des contrastes saisissants et une lisibilité absolue.",
            icon = Icons.Default.FlashOn
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Latest Projects Grid Summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(title = "Derniers Projets")
            Text(
                text = "Voir tout",
                color = NeonGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { viewModel.navigateToTab(1) } // Go to Portfolio
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Take 3 random or top latest projects and display them
        val latestProjects = viewModel.projects.take(3)
        latestProjects.forEach { project ->
            HomeProjectCard(
                project = project,
                onClick = {
                    viewModel.setSelectedProject(project)
                    viewModel.navigateToTab(1) // Redirect to portfolio to inspect detail dialog
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SpecialtyRow(
    title: String,
    desc: String,
    icon: ImageVector
) {
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
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeonGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = NeonGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PureWhite,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun HomeProjectCard(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column {
            // Stylized high quality Vector Placeholder instead of broken web image, displaying cyberpunk color palettes
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                BorderColor,
                                CardBackground
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Diagonal neon branding bar representing graphic design precision
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.25f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 6f
                    )
                    drawLine(
                        color = SoftGreen.copy(alpha = 0.15f),
                        start = Offset(size.width, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 3f
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Brush,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = project.category.uppercase(),
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PureWhite,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.shortDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateGray,
                    maxLines = 2
                )
            }
        }
    }
}
