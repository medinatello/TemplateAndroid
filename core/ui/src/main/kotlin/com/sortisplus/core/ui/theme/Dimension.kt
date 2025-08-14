package com.sortisplus.core.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Design System Dimensions following Material 3 spacing guidelines
 * All spacing values are based on 4dp grid system
 */
object Dimensions {

    // Basic Spacing Scale (4dp grid)
    val space0 = 0.dp
    val space1 = 4.dp
    val space2 = 8.dp
    val space3 = 12.dp
    val space4 = 16.dp
    val space5 = 20.dp
    val space6 = 24.dp
    val space7 = 28.dp
    val space8 = 32.dp
    val space10 = 40.dp
    val space12 = 48.dp
    val space16 = 64.dp
    val space20 = 80.dp
    val space24 = 96.dp

    // Semantic Spacing - Component specific
    val paddingExtraSmall = space1     // 4dp
    val paddingSmall = space2          // 8dp
    val paddingMedium = space4         // 16dp
    val paddingLarge = space6          // 24dp
    val paddingExtraLarge = space8     // 32dp

    // Margins
    val marginExtraSmall = space1      // 4dp
    val marginSmall = space2           // 8dp
    val marginMedium = space4          // 16dp
    val marginLarge = space6           // 24dp
    val marginExtraLarge = space8      // 32dp

    // Component Dimensions
    val buttonHeight = space12         // 48dp - Min touch target
    val buttonHeightSmall = space10    // 40dp - Compact buttons
    val textFieldHeight = space12      // 48dp - Standard text fields
    val iconSize = space6              // 24dp - Standard icons
    val iconSizeSmall = space5         // 20dp - Small icons
    val iconSizeLarge = space8         // 32dp - Large icons

    // Touch Targets (Accessibility)
    val minTouchTarget = space12       // 48dp - WCAG AA minimum
    val preferredTouchTarget = space12 // 48dp - Material recommendation

    // Border & Stroke
    val borderWidthThin = 1.dp
    val borderWidthMedium = 2.dp
    val borderWidthThick = 4.dp

    // Corner Radius
    val cornerRadiusNone = 0.dp
    val cornerRadiusSmall = space1     // 4dp
    val cornerRadiusMedium = space2    // 8dp
    val cornerRadiusLarge = space3     // 12dp
    val cornerRadiusExtraLarge = space4 // 16dp
    val cornerRadiusRound = 50.dp      // Fully rounded (pills, FAB)

    // Elevation (for shadow consistency)
    val elevationNone = 0.dp
    val elevationLow = 1.dp
    val elevationMedium = 3.dp
    val elevationHigh = 6.dp
    val elevationVeryHigh = 12.dp

    // Layout Dimensions
    val screenPadding = space4         // 16dp - Standard screen padding
    val cardPadding = space4           // 16dp - Card internal padding
    val listItemHeight = space12       // 48dp - Standard list item height
    val listItemHeightLarge = space16  // 64dp - Large list items

    // Form Elements
    val inputSpacing = space4          // 16dp - Spacing between form elements
    val labelSpacing = space1          // 4dp - Space between label and input
    val errorSpacing = space1          // 4dp - Space for error messages

    // Additional spacing
    val space14 = 56.dp                // 56dp - For navigation components
    
    // Navigation
    val bottomNavHeight = space14      // 56dp - Bottom navigation height
    val topAppBarHeight = space14      // 56dp - Top app bar height
    val tabHeight = space12            // 48dp - Tab height
}
