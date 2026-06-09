package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.NeonButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Project

@Composable
fun PortfolioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedPortfolioCategory.collectAsState()
    val selectedProject by viewModel.selectedProject.collectAsState()

    // Filter projects based on selected filter
    val filteredProjects = if (selectedCategory == "Tous") {
        viewModel.projects
    } else {
        viewModel.projects.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    val categories = listOf("Tous", "Affiches", "Flyers", "Branding", "Mock-ups")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "GALERIE PORTFOLIO",
            style = MaterialTheme.typography.displayMedium,
            color = PureWhite,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("portfolio_header_title")
        )
        Text(
            text = "Explorez les créations de Mr Koffi",
            color = NeonGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Category Row Filter
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) NeonGreen else CardBackground)
                        .border(1.dp, if (isSelected) NeonGreen else BorderColor, RoundedCornerShape(20.dp))
                        .clickable { viewModel.setPortfolioCategory(category) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("portfolio_filter_$category"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.Black else PureWhite,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Projects Grid
        if (filteredProjects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aucun projet trouvé dans cette catégorie.",
                    color = SlateGray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("portfolio_grid"),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProjects) { project ->
                    ProjectGridItem(
                        project = project,
                        onClick = { viewModel.setSelectedProject(project) }
                    )
                }
            }
        }
    }

    // Modal Details Dialog
    selectedProject?.let { project ->
        ProjectDetailDialog(
            project = project,
            onDismiss = { viewModel.setSelectedProject(null) },
            onRequestQuote = {
                // Determine quote request product type
                val qType = when (project.category) {
                    "Affiches" -> "Affiche"
                    "Flyers" -> "Flyer"
                    "Branding" -> "Identité Visuelle"
                    "Mock-ups" -> "Mock-up 3D"
                    else -> "Autre"
                }
                viewModel.setQuoteProjectType(qType)
                viewModel.setSelectedProject(null) // Dismiss modal
                viewModel.navigateToTab(4) // Navigate to Contact/Profile View
            }
        )
    }
}

@Composable
fun ProjectGridItem(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("project_card_${project.id}"),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column {
            // Styled Graphic Visualizer with gradients & line drafting
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(DarkBackground, BorderColor)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.15f),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.15f),
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 2f
                    )
                    drawCircle(
                        color = NeonGreen.copy(alpha = 0.05f),
                        center = center,
                        radius = size.minDimension / 3
                    )
                }

                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = NeonGreen.copy(alpha = 0.8f),
                    modifier = Modifier.size(28.dp)
                )

                // Badge Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(NeonGreen)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = project.category.uppercase(),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = project.shortDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateGray,
                    fontSize = 12.sp,
                    maxLines = 2,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailDialog(
    project: Project,
    onDismiss: () -> Unit,
    onRequestQuote: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .testTag("project_detail_dialog"),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, NeonGreen)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with title & close icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_project_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = PureWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // High Tech Graphic Box representation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBackground)
                        .border(1.dp, BorderColor, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            color = NeonGreen.copy(alpha = 0.2f),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = 3f
                        )
                        drawLine(
                            color = NeonGreen.copy(alpha = 0.2f),
                            start = Offset(size.width, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 3f
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NeonGreen)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = project.category.uppercase(),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Full bio detailed text
                Text(
                    text = project.fullDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateGray,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Call Buttons
                NeonButton(
                    text = "Demander un devis",
                    onClick = onRequestQuote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_dialog_request_quote")
                )
            }
        }
    }
}
