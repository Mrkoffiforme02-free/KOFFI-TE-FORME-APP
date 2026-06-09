package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonSecondaryButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.Course
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CoursesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val boughtCourses by viewModel.boughtCourses.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "FORMATIONS EXCLUSIVES",
            style = MaterialTheme.typography.displayMedium,
            color = PureWhite,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("courses_header_title")
        )
        Text(
            text = "Développez vos compétences et multipliez vos ventes",
            color = NeonGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag("courses_list"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(viewModel.courses) { course ->
                // Check if already purchased
                val isPurchased = boughtCourses.any { it.courseId == course.id }
                // Check if already in cart
                val isInCart = cartItems.any { it.courseId == course.id }

                CourseCard(
                    course = course,
                    isPurchased = isPurchased,
                    isInCart = isInCart,
                    onAddToCart = { viewModel.addCourseToCart(course) },
                    onOpenChariow = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(course.urlChariow))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    isPurchased: Boolean,
    isInCart: Boolean,
    onAddToCart: () -> Unit,
    onOpenChariow: () -> Unit
) {
    // Calculate off percentage
    val discountPercent = ((course.oldPrice - course.price).toFloat() / course.oldPrice * 100).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("course_card_${course.id}"),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isPurchased) NeonGreen else BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Category/Sale tag & Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonGreen)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "-$discountPercent% REDUCTION",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    if (isPurchased) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NeonGreen.copy(alpha = 0.15f))
                                .border(1.dp, NeonGreen, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "ACTIVÉ",
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = SlateGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleLarge,
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Course Description text
            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyMedium,
                color = SlateGray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pricing layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${course.price} F CFA",
                    color = NeonGreen,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
                Text(
                    text = "${course.oldPrice} F CFA",
                    color = SlateGray,
                    textDecoration = TextDecoration.LineThrough,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive Actions
            if (isPurchased) {
                // If purchased, replace payment checkout button with active direct course console link button!
                Button(
                    onClick = onOpenChariow,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_course_access_owned_${course.id}")
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.School, contentDescription = null, tint = Color.Black)
                        Text(
                            text = "Accéder à ma formation",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Accéder sur Chariow button
                    IconButtonWithBorder(
                        text = "Voir Chariow",
                        onClick = onOpenChariow,
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("btn_course_chariow_${course.id}")
                    )

                    // Ajouter au panier button
                    if (isInCart) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardBackground)
                                .border(1.dp, SoftGreen, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Dans le panier",
                                color = SoftGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        Button(
                            onClick = onAddToCart,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("btn_course_buy_${course.id}")
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Ajouter",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconButtonWithBorder(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = BorderColor
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = PureWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Icon(
                imageVector = Icons.Default.Launch,
                contentDescription = null,
                tint = PureWhite,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
