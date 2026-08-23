package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ClaimStatus
import com.example.data.model.InsuranceClaimEntity
import com.example.data.model.InsurancePolicyEntity
import com.example.data.model.InsuranceType
import com.example.data.model.PolicyStatus
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

@Composable
fun InsuranceScreen(
    viewModel: BankViewModel,
    onNavigateToApplication: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val policies by viewModel.insurancePolicies.collectAsStateWithLifecycle()
    val claims by viewModel.insuranceClaims.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Active Coverage", "File a Claim")

    var isClaimDialogOpen by remember { mutableStateOf(false) }
    var selectedPolicyForClaim by remember { mutableStateOf<InsurancePolicyEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Text(
                text = "RISK MITIGATION & PROTECTION",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "SmartBank Insurance Suite",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NavyCard,
                contentColor = CyberCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CyberCyan,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) CyberCyan else TextMuted
                            )
                        }
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // Total Active Coverage Banner
            item {
                val totalCoverage = policies.sumOf { it.coverageAmount }
                val totalMonthly = policies.sumOf { it.monthlyPremium }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("TOTAL INSURED COVERAGE", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                                Text("3 ACTIVE POLICIES", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                        }
                        Text("$${"%,.0f".format(totalCoverage)}", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                        Text("Monthly Premium: $${"%,.2f".format(totalMonthly)} / month auto-debited", color = CyberCyan, fontSize = 12.sp)
                    }
                }
            }

            items(policies) { policy ->
                PolicyItemCard(
                    policy = policy,
                    onClaimClick = {
                        selectedPolicyForClaim = policy
                        isClaimDialogOpen = true
                    }
                )
            }

            item {
                Button(
                    onClick = onNavigateToApplication,
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = TextWhite)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apply for New Insurance Policy", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Tab 2: Claims History & Instant Claim Wizard
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("File an Insurance Claim", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Submit claims with zero paperwork. AI triage reviews claims in minutes.", color = TextMuted, fontSize = 12.sp)
                        Button(
                            onClick = {
                                selectedPolicyForClaim = policies.firstOrNull()
                                isClaimDialogOpen = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Text("Start New Claim Wizard", color = Navy900, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Text("Claims History & Status", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            if (claims.isEmpty()) {
                item {
                    Surface(color = Navy900, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                        Text(
                            text = "No pending or historical claims. All policies in good standing.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(claims) { claim ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Claim for ${claim.policyType.name}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(claim.description, color = TextMuted, fontSize = 11.sp)
                                Text("Amount: $${"%,.2f".format(claim.claimAmount)}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Surface(
                                color = when (claim.status) {
                                    ClaimStatus.APPROVED -> EmeraldSuccess.copy(alpha = 0.2f)
                                    ClaimStatus.REJECTED -> CrimsonDanger.copy(alpha = 0.2f)
                                    else -> AmberOrange.copy(alpha = 0.2f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = claim.status.name,
                                    color = when (claim.status) {
                                        ClaimStatus.APPROVED -> EmeraldSuccess
                                        ClaimStatus.REJECTED -> CrimsonDanger
                                        else -> AmberOrange
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Claim Dialog
    if (isClaimDialogOpen && selectedPolicyForClaim != null) {
        val policy = selectedPolicyForClaim!!
        var claimAmtText by remember { mutableStateOf("1500.00") }
        var claimDateText by remember { mutableStateOf("Aug 18, 2026") }
        var claimDescText by remember { mutableStateOf("Emergency hospital treatment & diagnostics") }

        Dialog(onDismissRequest = { isClaimDialogOpen = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(0.95f),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("File Claim: ${policy.planName}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Policy Number: ${policy.policyNumber}", color = TextMuted, fontSize = 11.sp)

                    OutlinedTextField(
                        value = claimAmtText,
                        onValueChange = { claimAmtText = it },
                        label = { Text("Claim Amount ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = claimDescText,
                        onValueChange = { claimDescText = it },
                        label = { Text("Incident Description & Hospital/Repairer") },
                        singleLine = false,
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = {
                            val amt = claimAmtText.toDoubleOrNull() ?: 0.0
                            viewModel.submitInsuranceClaim(policy.id, policy.type, amt, claimDateText, claimDescText)
                            isClaimDialogOpen = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Text("Submit Claim to Underwriter", color = Navy900, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PolicyItemCard(
    policy: InsurancePolicyEntity,
    onClaimClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when (policy.type) {
                                    InsuranceType.HEALTH -> CrimsonDanger.copy(alpha = 0.2f)
                                    InsuranceType.CYBER_SHIELD -> CyberCyan.copy(alpha = 0.2f)
                                    InsuranceType.AUTO -> AmberOrange.copy(alpha = 0.2f)
                                    else -> ElectricBlue.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (policy.type) {
                                InsuranceType.HEALTH -> Icons.Default.HealthAndSafety
                                InsuranceType.CYBER_SHIELD -> Icons.Default.Security
                                InsuranceType.AUTO -> Icons.Default.DirectionsCar
                                else -> Icons.Default.Policy
                            },
                            contentDescription = null,
                            tint = when (policy.type) {
                                InsuranceType.HEALTH -> CrimsonDanger
                                InsuranceType.CYBER_SHIELD -> CyberCyan
                                InsuranceType.AUTO -> AmberOrange
                                else -> ElectricBlue
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(policy.planName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("${policy.policyNumber} • ${policy.provider}", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }

            Surface(color = Navy900, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Coverage Limit", color = TextMuted, fontSize = 10.sp)
                        Text("$${"%,.0f".format(policy.coverageAmount)}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Premium / Auto-Debit", color = TextMuted, fontSize = 10.sp)
                        Text("$${"%,.2f".format(policy.monthlyPremium)} /mo", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Text("• ${policy.keyFeatures}", color = TextMuted, fontSize = 11.sp)

            Button(
                onClick = onClaimClick,
                colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp)
            ) {
                Text("File a Claim under this Policy", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
