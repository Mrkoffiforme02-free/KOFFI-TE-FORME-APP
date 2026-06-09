package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.ui.components.SectionTitle
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AboutScreen(
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
        Spacer(modifier = Modifier.height(16.dp))

        // Professional Headshot Avatar
        Box(
            modifier = Modifier
                .size(110.dp)
                .testTag("about_profile_avatar"),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(2.dp, NeonGreen, CircleShape)
                    .background(CardBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Titles
        Text(
            text = "Cyrus Baudelaire TOSSOU",
            style = MaterialTheme.typography.displayMedium,
            color = PureWhite,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("about_profile_name")
        )

        Text(
            text = "Graphiste | Entrepreneur | Formateur",
            color = NeonGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bio Text
        Text(
            text = "Passionné d'art visuel et d'indépendance financière, je me spécialise depuis plusieurs années dans le design graphique mobile. J'aide les marques, commerçants de proximité et infopreneurs à asseoir leur crédibilité digitale grâce à des chartes et identités visuelles remarquables adaptées de bout en bout.\n\nEn tant que formateur, mon but est de démocratiser le design graphique et de montrer que la créativité professionnelle n'exige pas de matériel onéreux : seul un smartphone et une forte dose d'ambition technique suffisent pour créer des visuels dignes des plus grandes agences.",
            color = SlateGray,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Skills grid
        SectionTitle(title = "Compétences", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SkillBadge(title = "Graphisme Mobile", icon = Icons.Default.PhoneAndroid)
                SkillBadge(title = "Design Digital", icon = Icons.Default.Palette)
                SkillBadge(title = "Marketing Digital", icon = Icons.Default.TrendingUp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SkillBadge(title = "Création de Contenu", icon = Icons.Default.Movie)
                SkillBadge(title = "Communication", icon = Icons.Default.RecordVoiceOver)
                SkillBadge(title = "E-commerce", icon = Icons.Default.Store)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Achievements stats row
        SectionTitle(title = "Réalisations", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), title = "500+", subtitle = "Clients satisfaits")
            StatCard(modifier = Modifier.weight(1f), title = "100+", subtitle = "Projets réalisés")
            StatCard(modifier = Modifier.weight(1f), title = "5", subtitle = "Formations créées")
            StatCard(modifier = Modifier.weight(1f), title = "4.9/5", subtitle = "Note moyenne")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Timeline Path
        SectionTitle(title = "Mon Parcours", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(20.dp))

        TimelineSection()

        Spacer(modifier = Modifier.height(32.dp))

        // Brand Values
        SectionTitle(title = "Nos Valeurs", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))

        ValueRow(title = "Créativité", desc = "Dépasser continuellement les formats standards pour imaginer de nouveaux visuels uniques.")
        Spacer(modifier = Modifier.height(12.dp))
        ValueRow(title = "Accessibilité", desc = "Prouver que le graphisme professionnel s'adapte à chacun via des technologies mobiles légères.")
        Spacer(modifier = Modifier.height(12.dp))
        ValueRow(title = "Qualité", desc = "Apporter un soin chirurgical à chaque pixel, chaque contraste et chaque finition typographique.")
        Spacer(modifier = Modifier.height(12.dp))
        ValueRow(title = "Impact", desc = "Concevoir des identités à fort taux de clic, convertissant directement les vues en ventes concrètes.")

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun SkillBadge(
    title: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String
) {
    Card(
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = NeonGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = SlateGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun TimelineSection() {
    val items = listOf(
        Pair("2020", "Débuts passionnés dans le graphisme mobile et exploration d'outils mobiles de création."),
        Pair("2022", "Lancement officiel de formations Pixellab avec plus de 100 inscriptions en Afrique francophone."),
        Pair("2023", "Création de la marque 'Mr Koffi', collaborations internationales et expansions e-commerce."),
        Pair("2025", "Leader formateur avec plus de 500 clients satisfaits et accompagnement sur-mesure d'entreprises.")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items.forEachIndexed { index, pair ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Left Column: Dot connectors
                Column(
                    modifier = Modifier.width(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Small glowing dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(NeonGreen)
                    )

                    // Connecting vertical line if not the last item
                    if (index < items.size - 1) {
                        Canvas(
                            modifier = Modifier
                                .width(2.dp)
                                .height(60.dp)
                        ) {
                            drawLine(
                                color = BorderColor,
                                start = Offset(size.width / 2, 0f),
                                end = Offset(size.width / 2, size.height),
                                strokeWidth = 4f
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Right Column: Year title & description
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 20.dp)
                ) {
                    Text(
                        text = pair.first,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pair.second,
                        color = SlateGray,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ValueRow(
    title: String,
    desc: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title.uppercase(),
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = desc,
                color = SlateGray,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 18.sp
            )
        }
    }
}
