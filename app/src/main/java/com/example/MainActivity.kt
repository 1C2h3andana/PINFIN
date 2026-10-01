package com.example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.model.UserRole
import com.example.ui.components.AdminOnlyGuard
import com.example.ui.components.AuthDialog
import com.example.ui.components.BiometricAppLockOverlay
import com.example.ui.components.BiometricScreenGuard
import com.example.ui.components.DepositWithdrawDialog
import com.example.ui.components.NavigationSidebarContent
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.PulsingStatusBadge
import com.example.ui.components.SecurityClearanceLevel
import com.example.ui.components.SecurityContextProvider
import com.example.ui.components.StatementExportDialog
import com.example.ui.components.QuickActionFabMenu
import com.example.ui.screens.AboutPfinScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AiAdvisorScreen
import com.example.ui.screens.AiInsightsDashboardScreen
import com.example.ui.screens.AiPersonalLifePlannerScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.ApplicationFormsHubScreen
import com.example.ui.screens.AuthPageType
import com.example.ui.screens.AuthSecurityHubScreen
import com.example.ui.screens.BillsScreen
import com.example.ui.screens.BiometricLoginScreen
import com.example.ui.screens.BiometricAuthenticationScreen
import com.example.ui.screens.BlogInsightsScreen
import com.example.ui.screens.CareersScreen
import com.example.ui.screens.ContactUsScreen
import com.example.ui.screens.DeviceVerificationScreen
import com.example.ui.screens.FaceRecognitionScreen
import com.example.ui.screens.FaqsScreen
import com.example.ui.screens.FeaturesOverviewScreen
import com.example.ui.screens.FinancialHealthDashboardScreen
import com.example.ui.screens.ForgotPasswordScreen
import com.example.ui.screens.FutureFinancialOsScreen
import com.example.ui.screens.FuturePillarsSet1Screen
import com.example.ui.screens.FuturePillarsSet2Screen
import com.example.ui.screens.FuturePillarsSet3Screen
import com.example.ui.screens.GlobalCrisisIntelligenceScreen
import com.example.ui.screens.GlobalDashboardScreen
import com.example.ui.screens.GlobalEconomicEngineScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IndustriesScreen
import com.example.ui.screens.InsuranceScreen
import com.example.ui.screens.InvestmentsScreen
import com.example.ui.screens.LandingPageScreen
import com.example.ui.screens.LiteracyStudentHubScreen
import com.example.ui.screens.LoanCreditScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MfaVerificationScreen
import com.example.ui.screens.NotificationsDashboardScreen
import com.example.ui.screens.OtpVerificationScreen
import com.example.ui.screens.PersonalDashboardScreen
import com.example.ui.screens.PersonalizedRecommendationsDashboardScreen
import com.example.ui.screens.PaidEditionScreen
import com.example.ui.screens.PricingScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.RecentActivitiesDashboardScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.ResearchCenterScreen
import com.example.ui.screens.ResetPasswordScreen
import com.example.ui.screens.SecurityCenterScreen
import com.example.ui.screens.SecurityQuestionsScreen
import com.example.ui.screens.SolutionsScreen
import com.example.ui.screens.SuccessStoriesScreen
import com.example.ui.screens.TermsConditionsScreen
import com.example.ui.screens.TransferScreen
import com.example.ui.screens.UniversalWealthEngineScreen
import com.example.ui.screens.UserDashboardHubScreen
import com.example.ui.screens.UserDashboardPageType
import com.example.ui.screens.UserProfileHubScreen
import com.example.ui.screens.UserProfilePageType
import com.example.ui.screens.PersonalInformationScreen
import com.example.ui.screens.DigitalIdentityProfileScreen
import com.example.ui.screens.DocumentsVaultScreen
import com.example.ui.screens.EmploymentInformationScreen
import com.example.ui.screens.EducationProfileScreen
import com.example.ui.screens.FamilyInformationScreen
import com.example.ui.screens.AddressManagementScreen
import com.example.ui.screens.ContactInformationScreen
import com.example.ui.screens.TrustedDevicesScreen
import com.example.ui.screens.PrivacySettingsScreen
import com.example.ui.screens.DigitalIdentityHubScreen
import com.example.ui.screens.DigitalIdentityPageType
import com.example.ui.screens.UniversalDigitalIdentityScreen
import com.example.ui.screens.IdentityVerificationCenterScreen
import com.example.ui.screens.DigitalSignatureSuiteScreen
import com.example.ui.screens.PassportVerificationScreen
import com.example.ui.screens.DriverLicenseVerificationScreen
import com.example.ui.screens.NationalIdVerificationScreen
import com.example.ui.screens.KycVerificationFlowScreen
import com.example.ui.screens.FaceMatchingBiometricScreen
import com.example.ui.screens.IdentityHistoryAuditScreen
import com.example.ui.screens.BankingModuleHubScreen
import com.example.ui.screens.BankingModulePageType
import com.example.ui.screens.AccountSummaryScreen
import com.example.ui.screens.BalanceOverviewScreen
import com.example.ui.screens.DepositOperationsScreen
import com.example.ui.screens.WithdrawalOperationsScreen
import com.example.ui.screens.DomesticMoneyTransferScreen
import com.example.ui.screens.InternationalTransferScreen
import com.example.ui.screens.ScheduledPaymentsScreen
import com.example.ui.screens.StandingInstructionsScreen
import com.example.ui.screens.BeneficiaryManagementScreen
import com.example.ui.screens.AccountStatementsScreen
import com.example.ui.screens.TransactionModuleHubScreen
import com.example.ui.screens.TransactionPageType
import com.example.ui.screens.AiDigitalTwinSuiteScreen
import com.example.ui.screens.DigitalTwinPageType
import com.example.ui.screens.FinancialIntelligenceSuiteScreen
import com.example.ui.screens.FinancialIntelPageType
import com.example.ui.screens.WealthAndBudgetSuiteScreen
import com.example.ui.screens.WealthBudgetPageType
import com.example.ui.screens.InvestmentLoanInsuranceSuiteScreen
import com.example.ui.screens.InvestLoanInsurancePageType
import com.example.ui.screens.SecurityFraudGovernanceSuiteScreen
import com.example.ui.screens.SecurityFraudPageType
import com.example.ui.screens.MacroClimateHealthEducationSuiteScreen
import com.example.ui.screens.MacroClimateHealthEduPageType
import com.example.ui.screens.EnterprisePublicFutureSuiteScreen
import com.example.ui.screens.EnterpriseFuturePageType
import com.example.ui.screens.VisionMissionScreen
import com.example.ui.screens.WorldDigitalIdentityScreen
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Dashboard", Icons.Default.Home)
    object Transfer : Screen("transfer", "Transfer", Icons.Default.Send)
    object Security : Screen("security", "Security", Icons.Default.Security)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.TrendingUp)
    object Loans : Screen("loans", "Loans & EMI", Icons.Default.Calculate)
    object Advisor : Screen("advisor", "AI Concierge", Icons.Default.AutoAwesome)
    object Investments : Screen("investments", "Investments", Icons.Default.TrendingUp)
    object Insurance : Screen("insurance", "Insurance", Icons.Default.Policy)
    object Applications : Screen("applications", "Apply / KYC", Icons.Default.Description)
    object Admin : Screen("admin", "Admin Panel", Icons.Default.AdminPanelSettings)
    object Bills : Screen("bills", "Bills", Icons.Default.Receipt)
    object Literacy : Screen("literacy", "Academy", Icons.Default.School)
    object GlobalEconomy : Screen("global_economy", "Global Macro", Icons.Default.Public)
    object WorldIdentity : Screen("world_identity", "World ID", Icons.Default.Fingerprint)
    object LifePlanner : Screen("life_planner", "Life Planner", Icons.Default.Psychology)
    object WealthEngine : Screen("wealth_engine", "Wealth Engine", Icons.Default.AutoAwesome)
    object CrisisIntelligence : Screen("crisis_intelligence", "Crisis AI", Icons.Default.CrisisAlert)
    object FutureOs : Screen("future_os", "Financial OS", Icons.Default.RocketLaunch)
    object PillarsPart1 : Screen("pillars_part1", "Pillars 7-11", Icons.Default.Park)
    object PillarsPart2 : Screen("pillars_part2", "Pillars 12-16", Icons.Default.RocketLaunch)
    object PillarsPart3 : Screen("pillars_part3", "Pillars 17-20", Icons.Default.Storefront)

    // --- 15 PUBLIC PORTAL PAGES ---
    object Landing : Screen("landing", "Landing Page", Icons.Default.Home)
    object AboutPfin : Screen("about_pfin", "About PFIN", Icons.Default.Public)
    object VisionMission : Screen("vision_mission", "Vision & Mission", Icons.Default.Explore)
    object FeaturesOverview : Screen("features_overview", "Features Overview", Icons.Default.RocketLaunch)
    object Solutions : Screen("solutions", "Solutions", Icons.Default.Handshake)
    object Industries : Screen("industries", "Industries", Icons.Default.Apartment)
    object Pricing : Screen("pricing", "Pricing & Plans", Icons.Default.TrendingUp)
    object PaidEdition : Screen("paid_edition", "Paid Edition & License", Icons.Default.Star)
    object SuccessStories : Screen("success_stories", "Success Stories", Icons.Default.Star)
    object Blog : Screen("blog", "Blog & Insights", Icons.Default.MenuBook)
    object ResearchCenter : Screen("research_center", "Research Center", Icons.Default.Science)
    object ContactUs : Screen("contact_us", "Contact Us", Icons.Default.Email)
    object Careers : Screen("careers", "Careers", Icons.Default.Work)
    object Faqs : Screen("faqs", "FAQs", Icons.Default.HelpOutline)
    object Terms : Screen("terms", "Terms & Conditions", Icons.Default.Gavel)
    object Privacy : Screen("privacy", "Privacy Policy", Icons.Default.Security)

    // --- AUTHENTICATION & SECURITY SUITE PAGES ---
    object AuthHub : Screen("auth_hub", "Auth & Security", Icons.Default.Security)
    object AuthLogin : Screen("auth_login", "Login", Icons.Default.Security)
    object AuthRegister : Screen("auth_register", "Register", Icons.Default.Explore)
    object AuthForgotPassword : Screen("auth_forgot_password", "Forgot Password", Icons.Default.HelpOutline)
    object AuthResetPassword : Screen("auth_reset_password", "Reset Password", Icons.Default.Security)
    object AuthOtp : Screen("auth_otp", "OTP Verification", Icons.Default.Calculate)
    object AuthMfa : Screen("auth_mfa", "MFA Authentication", Icons.Default.Security)
    object AuthBiometric : Screen("auth_biometric", "Biometric Login", Icons.Default.Fingerprint)
    object AuthFaceScan : Screen("auth_face_scan", "Face Recognition", Icons.Default.Explore)
    object AuthDeviceVerify : Screen("auth_device_verify", "Device Verification", Icons.Default.Description)
    object AuthSecurityQuestions : Screen("auth_security_questions", "Security Questions", Icons.Default.HelpOutline)

    // --- USER DASHBOARD SUITE PAGES ---
    object DashboardHub : Screen("dashboard_hub", "Dashboards 360°", Icons.Default.AccountBalance)
    object DashboardGlobal : Screen("dashboard_global", "Global Dashboard", Icons.Default.Public)
    object DashboardPersonal : Screen("dashboard_personal", "Personal Dashboard", Icons.Default.AccountBalance)
    object DashboardHealth : Screen("dashboard_health", "Financial Health", Icons.Default.Security)
    object DashboardInsights : Screen("dashboard_insights", "AI Insights", Icons.Default.AutoAwesome)
    object DashboardNotifications : Screen("dashboard_notifications", "Notifications", Icons.Default.Notifications)
    object DashboardActivities : Screen("dashboard_activities", "Recent Activities", Icons.Default.Receipt)
    object DashboardRecommendations : Screen("dashboard_recommendations", "AI Recommendations", Icons.Default.Star)

    // --- USER PROFILE SUITE PAGES ---
    object ProfileHub : Screen("profile_hub", "User Profile Hub", Icons.Default.Security)
    object ProfilePersonal : Screen("profile_personal", "Personal Info", Icons.Default.Description)
    object ProfileDigitalId : Screen("profile_digital_id", "Digital ID (DID)", Icons.Default.Fingerprint)
    object ProfileDocuments : Screen("profile_documents", "Documents & KYC", Icons.Default.Receipt)
    object ProfileEmployment : Screen("profile_employment", "Employment", Icons.Default.Work)
    object ProfileEducation : Screen("profile_education", "Education", Icons.Default.School)
    object ProfileFamily : Screen("profile_family", "Family & Beneficiaries", Icons.Default.Star)
    object ProfileAddress : Screen("profile_address", "Addresses", Icons.Default.Home)
    object ProfileContact : Screen("profile_contact", "Contact & PGP", Icons.Default.Email)
    object ProfileDevices : Screen("profile_devices", "Trusted Devices", Icons.Default.Science)
    object ProfilePrivacy : Screen("profile_privacy", "Privacy & ZKP", Icons.Default.Policy)

    // --- DIGITAL IDENTITY SUITE PAGES ---
    object DidHub : Screen("did_hub", "Digital Identity Hub", Icons.Default.Fingerprint)
    object DidUniversal : Screen("did_universal", "Universal DID", Icons.Default.Fingerprint)
    object DidVerification : Screen("did_verification", "Identity Verification", Icons.Default.Security)
    object DidSignature : Screen("did_signature", "Digital Signature", Icons.Default.Description)
    object DidPassport : Screen("did_passport", "Passport Verification", Icons.Default.Receipt)
    object DidDriverLicense : Screen("did_driver_license", "Driver License", Icons.Default.Description)
    object DidNationalId : Screen("did_national_id", "National ID", Icons.Default.Policy)
    object DidKyc : Screen("did_kyc", "KYC Verification", Icons.Default.Security)
    object DidFaceMatching : Screen("did_face_matching", "Face Matching", Icons.Default.Psychology)
    object DidHistory : Screen("did_history", "Identity History", Icons.Default.Receipt)

    // --- BANKING MODULE SUITE PAGES (10 PAGES) ---
    object BankingHub : Screen("banking_hub", "Banking Module Hub", Icons.Default.AccountBalance)
    object BankingSummary : Screen("banking_summary", "Account Summary", Icons.Default.AccountBalance)
    object BankingBalances : Screen("banking_balances", "Balance Overview", Icons.Default.TrendingUp)
    object BankingDeposit : Screen("banking_deposit", "Deposit", Icons.Default.Calculate)
    object BankingWithdrawal : Screen("banking_withdrawal", "Withdrawal", Icons.Default.Receipt)
    object BankingTransfer : Screen("banking_transfer", "Money Transfer", Icons.Default.Send)
    object BankingInternational : Screen("banking_international", "International Transfer", Icons.Default.Public)
    object BankingScheduled : Screen("banking_scheduled", "Scheduled Payments", Icons.Default.RocketLaunch)
    object BankingStanding : Screen("banking_standing", "Standing Instructions", Icons.Default.AutoAwesome)
    object BankingBeneficiaries : Screen("banking_beneficiaries", "Beneficiary Management", Icons.Default.Handshake)
    object BankingStatements : Screen("banking_statements", "Account Statements", Icons.Default.Description)

    // --- MODULE 7: TRANSACTIONS (8 PAGES) ---
    object TxHub : Screen("tx_hub", "Transactions Hub", Icons.Default.Receipt)
    object TxHistory : Screen("tx_history", "Transaction History", Icons.Default.Receipt)
    object TxDetails : Screen("tx_details", "Transaction Details", Icons.Default.Description)
    object TxAnalytics : Screen("tx_analytics", "Transaction Analytics", Icons.Default.TrendingUp)
    object TxSearch : Screen("tx_search", "Search Transactions", Icons.Default.Search)
    object TxExportPdf : Screen("tx_export_pdf", "Export PDF", Icons.Default.PictureAsPdf)
    object TxExportCsv : Screen("tx_export_csv", "Export CSV", Icons.Default.FileDownload)
    object TxCategories : Screen("tx_categories", "Transaction Categories", Icons.Default.Category)
    object TxTimeline : Screen("tx_timeline", "Spending Timeline", Icons.Default.Timeline)

    // --- MODULE 8: AI FINANCIAL DIGITAL TWIN (9 PAGES) ---
    object TwinHub : Screen("twin_hub", "AI Digital Twin Hub", Icons.Default.Psychology)
    object TwinSimulation : Screen("twin_sim", "Monte Carlo Simulation", Icons.Default.AutoAwesome)
    object TwinFutureWealth : Screen("twin_wealth", "Future Wealth Prediction", Icons.Default.TrendingUp)
    object TwinRetirement : Screen("twin_retire", "Retirement Simulation", Icons.Default.BeachAccess)
    object TwinLoan : Screen("twin_loan", "Loan Simulation", Icons.Default.Calculate)
    object TwinEducation : Screen("twin_edu", "Education Cost Simulation", Icons.Default.School)
    object TwinFamily : Screen("twin_family", "Family Planning Simulation", Icons.Default.FamilyRestroom)
    object TwinEmergency : Screen("twin_emergency", "Emergency Planning", Icons.Default.HealthAndSafety)
    object TwinInvestment : Screen("twin_invest", "Investment Simulation", Icons.Default.RocketLaunch)

    // --- MODULE 9: FINANCIAL INTELLIGENCE (8 PAGES) ---
    object FinIntelHub : Screen("fin_intel_hub", "Financial Intel Hub", Icons.Default.Insights)
    object FinIntelScore : Screen("fin_intel_score", "Intel Score", Icons.Default.AutoAwesome)
    object FinHealthScore : Screen("fin_health_score", "Health Score", Icons.Default.HealthAndSafety)
    object FinCreditHealth : Screen("fin_credit_health", "Credit Health", Icons.Default.Speed)
    object FinDebtAnalysis : Screen("fin_debt_analysis", "Debt Analysis", Icons.Default.MoneyOff)
    object FinIncomeAnalysis : Screen("fin_income_analysis", "Income Analysis", Icons.Default.TrendingUp)
    object FinSpendingAnalysis : Screen("fin_spending_analysis", "Spending Analysis", Icons.Default.PieChart)
    object FinWealthGrowth : Screen("fin_wealth_growth", "Wealth Growth", Icons.Default.ShowChart)
    object FinReports : Screen("fin_reports", "Financial Reports", Icons.Default.Description)

    // --- MODULE 10 & 11: WEALTH & BUDGET SUITE (15 PAGES) ---
    object WealthBudgetMasterHub : Screen("wealth_budget_hub", "Wealth & Budget Hub", Icons.Default.AccountBalanceWallet)
    object WealthDashboardPage : Screen("wealth_dashboard", "Wealth Dashboard", Icons.Default.AccountBalanceWallet)
    object WealthSavingsPlanner : Screen("wealth_savings", "Savings Planner", Icons.Default.Savings)
    object WealthInvestmentPlanner : Screen("wealth_invest_planner", "Investment Planner", Icons.Default.TrendingUp)
    object WealthRetirementPlanner : Screen("wealth_retire_planner", "Retirement Planner", Icons.Default.BeachAccess)
    object WealthTaxOpt : Screen("wealth_tax", "Tax Optimization", Icons.Default.Policy)
    object WealthExpenseOpt : Screen("wealth_expense_opt", "Expense Optimizer", Icons.Default.AutoAwesome)
    object WealthPassiveIncome : Screen("wealth_passive", "Passive Income", Icons.Default.AttachMoney)
    object WealthNetWorthCalc : Screen("wealth_networth_calc", "Net Worth Calculator", Icons.Default.Calculate)
    object BudgetDashboardPage : Screen("budget_dashboard", "Budget Dashboard", Icons.Default.PieChart)
    object BudgetMonthlyPage : Screen("budget_monthly", "Monthly Budget", Icons.Default.CalendarMonth)
    object BudgetCategoriesPage : Screen("budget_categories", "Expense Categories", Icons.Default.Category)
    object BudgetGoalsPage : Screen("budget_goals", "Goal Tracking", Icons.Default.Flag)
    object BudgetSavingsGoalsPage : Screen("budget_savings_goals", "Savings Goals", Icons.Default.Star)
    object BudgetAlertsPage : Screen("budget_alerts", "Spending Alerts", Icons.Default.NotificationsActive)
    object BudgetRecommendationsPage : Screen("budget_recommendations", "Smart Recommendations", Icons.Default.Lightbulb)

    // --- MODULE 12, 13, 14, 15: INVESTMENTS, LOANS, INSURANCE & COACH (22 PAGES) ---
    object InvestLoanMasterHub : Screen("invest_loan_hub", "Invest, Loans & Insurance Hub", Icons.Default.TrendingUp)
    object InvestPortfolioPage : Screen("invest_portfolio", "Portfolio Dashboard", Icons.Default.TrendingUp)
    object InvestStocksPage : Screen("invest_stocks", "Stock Investments", Icons.Default.ShowChart)
    object InvestCryptoPage : Screen("invest_crypto", "Cryptocurrency", Icons.Default.CurrencyBitcoin)
    object InvestBondsPage : Screen("invest_bonds", "Bonds & T-Bills", Icons.Default.Security)
    object InvestGoldPage : Screen("invest_gold", "Gold Investments", Icons.Default.Diamond)
    object InvestRealEstatePage : Screen("invest_realestate", "Real Estate", Icons.Default.Apartment)
    object InvestEsgPage : Screen("invest_esg", "ESG Investments", Icons.Default.Eco)
    object InvestRiskPage : Screen("invest_risk", "Portfolio Risk Analysis", Icons.Default.Speed)
    object LoanDashboardPage : Screen("loan_dashboard", "Loan Dashboard", Icons.Default.Calculate)
    object LoanEligibilityPage : Screen("loan_eligibility", "Loan Eligibility", Icons.Default.CheckCircle)
    object LoanEmiCalcPage : Screen("loan_emi_calc", "EMI Calculator", Icons.Default.Payments)
    object LoanAdvisorPage : Screen("loan_advisor", "AI Loan Advisor", Icons.Default.Psychology)
    object LoanRepaymentPage : Screen("loan_repayment", "Repayment Planner", Icons.Default.Schedule)
    object InsuranceDashboardPage : Screen("insurance_dashboard", "Insurance Dashboard", Icons.Default.Policy)
    object InsuranceHealthPage : Screen("insurance_health", "Health Insurance", Icons.Default.HealthAndSafety)
    object InsuranceVehiclePage : Screen("insurance_vehicle", "Vehicle Insurance", Icons.Default.DirectionsCar)
    object InsurancePropertyPage : Screen("insurance_property", "Property Insurance", Icons.Default.Home)
    object InsuranceClaimsPage : Screen("insurance_claims", "Claims Management", Icons.Default.Assignment)
    object CoachAssistantPage : Screen("coach_assistant", "Personal AI Coach", Icons.Default.AutoAwesome)
    object CoachInsightsPage : Screen("coach_insights", "Daily Insights", Icons.Default.Lightbulb)
    object CoachReportsPage : Screen("coach_reports", "Weekly Reports", Icons.Default.Description)
    object CoachEducationPage : Screen("coach_education", "Financial Education", Icons.Default.School)

    // --- MODULE 16, 17, 31, 37: SECURITY, FRAUD, ADMIN & COMPLIANCE (17 PAGES) ---
    object SecurityFortressHub : Screen("security_fortress_hub", "Security Fortress Hub", Icons.Default.Security)
    object FraudDashboardPage : Screen("fraud_dashboard", "Fraud Dashboard", Icons.Default.GppBad)
    object FraudLivePage : Screen("fraud_live", "Live Threat Feed", Icons.Default.Radar)
    object FraudSuspiciousPage : Screen("fraud_suspicious", "Suspicious TX", Icons.Default.Warning)
    object FraudPredictPage : Screen("fraud_predict", "AI Fraud Prediction", Icons.Default.Psychology)
    object CyberDashboardPage : Screen("cyber_dashboard", "Cyber Dashboard", Icons.Default.Security)
    object CyberLoginsPage : Screen("cyber_logins", "Login History", Icons.Default.History)
    object CyberDevicesPage : Screen("cyber_devices", "Trusted Devices", Icons.Default.Devices)
    object CyberThreatsPage : Screen("cyber_threats", "Threat Detection", Icons.Default.Shield)
    object AdminDashboardPage : Screen("admin_dashboard", "Admin 360°", Icons.Default.AdminPanelSettings)
    object AdminUserMgmtPage : Screen("admin_user_mgmt", "User Management", Icons.Default.People)
    object AdminRolesPage : Screen("admin_roles", "RBAC & Permissions", Icons.Default.Key)
    object AdminAuditPage : Screen("admin_audit", "Audit Logs", Icons.Default.ReceiptLong)
    object AdminBackupPage : Screen("admin_backup", "Backup & Recovery", Icons.Default.Backup)
    object ComplianceDashboardPage : Screen("compliance_dashboard", "Compliance Hub", Icons.Default.Policy)
    object ComplianceAmlPage : Screen("compliance_aml", "AML Monitoring", Icons.Default.Search)
    object ComplianceKycPage : Screen("compliance_kyc", "KYC Monitoring", Icons.Default.VerifiedUser)
    object ComplianceReportsPage : Screen("compliance_reports", "Regulatory Reports", Icons.Default.Description)

    // --- MODULE 18, 19, 20, 21: MACRO, CLIMATE, HEALTH & EDUCATION (13 PAGES) ---
    object MacroClimateHealthMasterHub : Screen("macro_climate_hub", "Macro & Planetary Hub", Icons.Default.Public)
    object MacroEconomyPage : Screen("macro_economy", "Global Macro", Icons.Default.Public)
    object MacroInflationPage : Screen("macro_inflation", "Inflation Tracker", Icons.Default.TrendingUp)
    object MacroCommoditiesPage : Screen("macro_commodities", "Commodity & Oil", Icons.Default.LocalGasStation)
    object MacroRatesPage : Screen("macro_rates", "Interest Rates", Icons.Default.AccountBalance)
    object ClimateDashboardPage : Screen("climate_dashboard", "Climate Hub", Icons.Default.Eco)
    object ClimateCarbonScorePage : Screen("climate_carbon_score", "Carbon Score", Icons.Default.EnergySavingsLeaf)
    object ClimateDisasterRiskPage : Screen("climate_disaster_risk", "Disaster Risk", Icons.Default.Flood)
    object HealthExpensePlanPage : Screen("health_expense_plan", "Medical Planner", Icons.Default.MedicalServices)
    object HospitalCostAiPage : Screen("hospital_cost_ai", "Hospital Cost AI", Icons.Default.LocalHospital)
    object HsaVaultPage : Screen("hsa_vault", "HSA Savings Vault", Icons.Default.HealthAndSafety)
    object EduPlannerCollegePage : Screen("edu_planner_college", "Education Planner", Icons.Default.School)
    object ScholarshipFinderPage : Screen("scholarship_finder", "Scholarship Finder", Icons.Default.CardGiftcard)
    object CareerPredictPage : Screen("career_predict", "Career Income AI", Icons.Default.Work)

    // --- MODULE 22-30, 32-36, 38: ENTERPRISE, GOVT & FUTURE ECONOMY (18 PAGES) ---
    object EnterpriseFutureMasterHub : Screen("enterprise_future_hub", "Enterprise & Future Hub", Icons.Default.CorporateFare)
    object BusinessHubPage : Screen("business_hub", "Business Intelligence", Icons.Default.Business)
    object BusinessPayrollPage : Screen("business_payroll", "Payroll Engine", Icons.Default.Payments)
    object BusinessInvoicesPage : Screen("business_invoices", "Invoices & AP", Icons.Default.Receipt)
    object SmartCityHubPage : Screen("smart_city_hub", "Smart City Finance", Icons.Default.LocationCity)
    object CityUtilitiesPage : Screen("city_utilities", "EV & Utilities", Icons.Default.ElectricBolt)
    object GovtPortalPage : Screen("govt_portal", "Government Portal", Icons.Default.AccountBalance)
    object DisasterReliefPage : Screen("disaster_relief", "Disaster Relief", Icons.Default.CrisisAlert)
    object HumanitarianNgoPage : Screen("humanitarian_ngo", "Humanitarian NGO", Icons.Default.VolunteerActivism)
    object SustainabilityCenterPage : Screen("sustainability_center", "Sustainability Center", Icons.Default.Eco)
    object ResearchInnovationPage : Screen("research_innovation", "Research Lab", Icons.Default.Science)
    object AiMarketplacePage : Screen("ai_marketplace", "AI Marketplace", Icons.Default.Storefront)
    object ExecutiveAnalyticsPage : Screen("executive_analytics", "Executive KPIs", Icons.Default.Analytics)
    object DeveloperCenterPage : Screen("developer_center", "Developer APIs", Icons.Default.Code)
    object FutureTechHubPage : Screen("future_tech_hub", "Future Tech Hub", Icons.Default.Biotech)
    object SpaceLunarEconomyPage : Screen("space_lunar_economy", "Space & Lunar Economy", Icons.Default.RocketLaunch)
    object NotificationCenterPage : Screen("notification_center", "Notifications Center", Icons.Default.Notifications)
    object SettingsCenterPage : Screen("settings_center", "Master Settings", Icons.Default.Settings)
    object HelpSupportCenterPage : Screen("help_support_center", "Help & AI Support", Icons.Default.SupportAgent)
}

class MainActivity : FragmentActivity() {
    private val viewModel: BankViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize notification channel and schedule periodic WorkManager background monitoring for suspicious transactions
        com.example.service.SuspiciousTransactionMonitoringWorker.createNotificationChannel(this)
        com.example.service.SuspiciousTransactionMonitoringWorker.schedulePeriodic(this)

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val isBiometricsEnabled by viewModel.isBiometricsEnabled.collectAsStateWithLifecycle()
            val isAppUnlocked by viewModel.isAppUnlocked.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkMode) {
                SecurityContextProvider(viewModel = viewModel) {
                    if (isBiometricsEnabled && !isAppUnlocked) {
                        BiometricAuthenticationScreen(
                            viewModel = viewModel,
                            onAuthSuccess = {
                                viewModel.unlockAppWithBiometrics("Android Biometric Hardware (BiometricPrompt)")
                            }
                        )
                    } else {
                        MainAppScaffold(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: BankViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMsg by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val unreadNotifsCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val depositWithdrawState by viewModel.depositWithdrawState.collectAsStateWithLifecycle()

    var isAuthDialogOpen by remember { mutableStateOf(false) }
    var isNotificationsDialogOpen by remember { mutableStateOf(false) }
    var isStatementDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    val primaryNavItems = listOf(
        Screen.Home,
        Screen.Transfer,
        Screen.Security,
        Screen.Analytics,
        Screen.Investments,
        Screen.Advisor
    )

    fun navigateTo(route: String) {
        coroutineScope.launch { drawerState.close() }
        if (currentRoute != route) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(320.dp)
            ) {
                NavigationSidebarContent(
                    currentRoute = currentRoute,
                    authState = authState,
                    onNavigate = { route -> navigateTo(route) },
                    onOpenAuth = {
                        coroutineScope.launch { drawerState.close() }
                        isAuthDialogOpen = true
                    },
                    isDarkMode = isDarkMode,
                    onToggleTheme = { viewModel.toggleDarkMode() }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("nav_open_sidebar_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Navigation Sidebar",
                                tint = CyberCyan
                            )
                        }
                    },
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable { isAuthDialogOpen = true }
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "SMARTBANK • PFIN",
                                        color = TextWhite,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    if (authState.currentUser?.role == UserRole.ADMIN) {
                                        Surface(color = AmberOrange.copy(alpha = 0.25f), shape = RoundedCornerShape(4.dp)) {
                                            Text("ADMIN", color = AmberOrange, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                                Text(
                                    text = "Zero-Trust Fraud Shield & Financial OS",
                                    color = CyberCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    actions = {
                        // Notifications with real Badge
                        IconButton(
                            onClick = { isNotificationsDialogOpen = true },
                            modifier = Modifier.testTag("nav_top_notifications_btn")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotifsCount > 0) {
                                        Badge(containerColor = CrimsonDanger) {
                                            Text("$unreadNotifsCount", color = TextWhite, fontSize = 10.sp)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = if (unreadNotifsCount > 0) CyberCyan else TextMuted
                                )
                            }
                        }

                        // Statement Export button
                        IconButton(
                            onClick = { isStatementDialogOpen = true },
                            modifier = Modifier.testTag("nav_top_statements_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Description, contentDescription = "Statements", tint = TextMuted)
                        }

                        // Quick Public Landing Shortcut
                        IconButton(
                            onClick = { navigateTo(Screen.Landing.route) },
                            modifier = Modifier.testTag("nav_top_landing_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Public, contentDescription = "Public Portal", tint = if (currentRoute == Screen.Landing.route) CyberCyan else TextMuted)
                        }

                        // Paid Edition & Pro License Button
                        IconButton(
                            onClick = { navigateTo(Screen.PaidEdition.route) },
                            modifier = Modifier.testTag("nav_top_paid_edition_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "PFIN Pro Edition",
                                tint = if (currentRoute == Screen.PaidEdition.route) AmberOrange else GoldAccent
                            )
                        }

                        // Future Financial OS (2050-2077) Master Hub
                        IconButton(
                            onClick = { navigateTo(Screen.FutureOs.route) },
                            modifier = Modifier.testTag("nav_top_future_os_btn")
                        ) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = "Financial OS 2077", tint = if (currentRoute == Screen.FutureOs.route) GoldAccent else TextMuted)
                        }

                        // Biometric Lock Financial Vault Action
                        IconButton(
                            onClick = { viewModel.lockAppBiometric() },
                            modifier = Modifier.testTag("nav_top_lock_vault_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Financial Vault with Biometrics",
                                tint = CyberCyan
                            )
                        }

                        // Dark mode toggle
                        IconButton(
                            onClick = { viewModel.toggleDarkMode() },
                            modifier = Modifier.testTag("toggle_dark_mode_btn")
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = GoldAccent
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Navy900,
                        titleContentColor = TextWhite
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = NavyCard,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    primaryNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { navigateTo(screen.route) },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Navy900,
                                selectedTextColor = CyberCyan,
                                indicatorColor = CyberCyan,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_${screen.route}")
                        )
                    }
                }
            },
            floatingActionButton = {
                val fabScreens = listOf(
                    Screen.Home.route,
                    Screen.Transfer.route,
                    Screen.Analytics.route,
                    Screen.Security.route,
                    Screen.Loans.route,
                    Screen.Investments.route
                )
                if (currentRoute in fabScreens) {
                    QuickActionFabMenu(
                        onAddTransaction = { viewModel.openDepositDialog() },
                        onCheckEligibility = { navigateTo(Screen.Loans.route) },
                        onViewAlerts = { isNotificationsDialogOpen = true },
                        onExportCsv = { isStatementDialogOpen = true },
                        onLockVault = { viewModel.lockAppBiometric() }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // --- CORE BANKING SCREENS ---
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateTransfer = { navController.navigate(Screen.Transfer.route) },
                        onNavigateSecurity = { navController.navigate(Screen.Security.route) },
                        onNavigateAiAdvisor = { navController.navigate(Screen.Advisor.route) },
                        onNavigateBills = { navController.navigate(Screen.Bills.route) },
                        onNavigateLoans = { navController.navigate(Screen.Loans.route) },
                        onNavigateInvestments = { navController.navigate(Screen.Investments.route) },
                        onNavigateInsurance = { navController.navigate(Screen.Insurance.route) },
                        onNavigateApplications = { navController.navigate(Screen.Applications.route) },
                        onNavigateAdmin = { navController.navigate(Screen.Admin.route) },
                        onOpenDeposit = { viewModel.openDepositDialog() },
                        onOpenWithdraw = { viewModel.openWithdrawDialog() },
                        onOpenStatements = { isStatementDialogOpen = true },
                        onOpenAuth = { isAuthDialogOpen = true },
                        onNavigateGlobalEconomy = { navController.navigate(Screen.GlobalEconomy.route) },
                        onNavigateWorldIdentity = { navController.navigate(Screen.WorldIdentity.route) },
                        onNavigateLifePlanner = { navController.navigate(Screen.LifePlanner.route) },
                        onNavigateWealthEngine = { navController.navigate(Screen.WealthEngine.route) },
                        onNavigateCrisisIntelligence = { navController.navigate(Screen.CrisisIntelligence.route) },
                        onNavigateFutureOs = { navController.navigate(Screen.FutureOs.route) },
                        onNavigateLanding = { navController.navigate(Screen.Landing.route) }
                    )
                }
                composable(Screen.Transfer.route) {
                    TransferScreen(viewModel = viewModel)
                }
                composable(Screen.Security.route) {
                    SecurityCenterScreen(viewModel = viewModel)
                }
                composable(Screen.Analytics.route) {
                    AnalyticsScreen(viewModel = viewModel)
                }
                composable(Screen.Loans.route) {
                    LoanCreditScreen(viewModel = viewModel)
                }
                composable(Screen.Advisor.route) {
                    AiAdvisorScreen(viewModel = viewModel)
                }
                composable(Screen.Investments.route) {
                    InvestmentsScreen(viewModel = viewModel)
                }
                composable(Screen.Insurance.route) {
                    InsuranceScreen(
                        viewModel = viewModel,
                        onNavigateToApplication = { navController.navigate(Screen.Applications.route) }
                    )
                }
                composable(Screen.Applications.route) {
                    ApplicationFormsHubScreen(viewModel = viewModel)
                }
                composable(Screen.Admin.route) {
                    AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) {
                        BiometricScreenGuard(
                            viewModel = viewModel,
                            screenTitle = "Root Admin Control Panel",
                            screenSubtitle = "Biometric clearance verification required for Root Ledger & Audit Enclave"
                        ) {
                            AdminPanelScreen(viewModel = viewModel)
                        }
                    }
                }
                composable(Screen.Bills.route) {
                    BillsScreen(viewModel = viewModel)
                }
                composable(Screen.Literacy.route) {
                    LiteracyStudentHubScreen(viewModel = viewModel)
                }
                composable(Screen.GlobalEconomy.route) {
                    GlobalEconomicEngineScreen(viewModel = viewModel)
                }
                composable(Screen.WorldIdentity.route) {
                    WorldDigitalIdentityScreen(viewModel = viewModel)
                }
                composable(Screen.LifePlanner.route) {
                    AiPersonalLifePlannerScreen(viewModel = viewModel)
                }
                composable(Screen.WealthEngine.route) {
                    UniversalWealthEngineScreen(viewModel = viewModel)
                }
                composable(Screen.CrisisIntelligence.route) {
                    GlobalCrisisIntelligenceScreen(viewModel = viewModel)
                }
                composable(Screen.FutureOs.route) {
                    FutureFinancialOsScreen(
                        viewModel = viewModel,
                        onNavigateAdvisor = { navController.navigate(Screen.Advisor.route) },
                        onNavigateGlobalEconomy = { navController.navigate(Screen.GlobalEconomy.route) },
                        onNavigateWorldIdentity = { navController.navigate(Screen.WorldIdentity.route) },
                        onNavigateLifePlanner = { navController.navigate(Screen.LifePlanner.route) },
                        onNavigateWealthEngine = { navController.navigate(Screen.WealthEngine.route) },
                        onNavigateCrisisIntelligence = { navController.navigate(Screen.CrisisIntelligence.route) },
                        onNavigatePillarsPart1 = { _ -> navController.navigate(Screen.PillarsPart1.route) },
                        onNavigatePillarsPart2 = { _ -> navController.navigate(Screen.PillarsPart2.route) },
                        onNavigatePillarsPart3 = { _ -> navController.navigate(Screen.PillarsPart3.route) }
                    )
                }
                composable(Screen.PillarsPart1.route) {
                    FuturePillarsSet1Screen(viewModel = viewModel)
                }
                composable(Screen.PillarsPart2.route) {
                    FuturePillarsSet2Screen(viewModel = viewModel)
                }
                composable(Screen.PillarsPart3.route) {
                    FuturePillarsSet3Screen(viewModel = viewModel)
                }

                // --- 15 DEDICATED PUBLIC PORTAL PAGES ---
                composable(Screen.Landing.route) {
                    LandingPageScreen(
                        viewModel = viewModel,
                        onNavigateToPublicPage = { targetRoute -> navController.navigate(targetRoute) },
                        onOpenAccount = { navController.navigate(Screen.Applications.route) }
                    )
                }
                composable(Screen.AboutPfin.route) {
                    AboutPfinScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.VisionMission.route) {
                    VisionMissionScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.FeaturesOverview.route) {
                    FeaturesOverviewScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onOpenPillar = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.Solutions.route) {
                    SolutionsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Industries.route) {
                    IndustriesScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Pricing.route) {
                    PricingScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToPaidEdition = { navController.navigate(Screen.PaidEdition.route) }
                    )
                }
                composable(Screen.PaidEdition.route) {
                    PaidEditionScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onOpenTerms = { navController.navigate(Screen.Terms.route) },
                        onOpenPrivacy = { navController.navigate(Screen.Privacy.route) }
                    )
                }
                composable(Screen.SuccessStories.route) {
                    SuccessStoriesScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Blog.route) {
                    BlogInsightsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.ResearchCenter.route) {
                    ResearchCenterScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.ContactUs.route) {
                    ContactUsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Careers.route) {
                    CareersScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Faqs.route) {
                    FaqsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Terms.route) {
                    TermsConditionsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Privacy.route) {
                    PrivacyPolicyScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }

                // --- AUTHENTICATION & SECURITY SUITE COMPOSABLES ---
                composable(Screen.AuthHub.route) {
                    AuthSecurityHubScreen(
                        viewModel = viewModel,
                        initialPage = AuthPageType.LOGIN,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.AuthLogin.route) {
                    LoginScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = { navController.navigate(Screen.AuthRegister.route) },
                        onNavigateToForgot = { navController.navigate(Screen.AuthForgotPassword.route) },
                        onNavigateToBiometric = { navController.navigate(Screen.AuthBiometric.route) },
                        onNavigateToFace = { navController.navigate(Screen.AuthFaceScan.route) }
                    )
                }
                composable(Screen.AuthRegister.route) {
                    RegisterScreen(
                        viewModel = viewModel,
                        onNavigateToLogin = { navController.navigate(Screen.AuthLogin.route) },
                        onNavigateToOtp = { navController.navigate(Screen.AuthOtp.route) }
                    )
                }
                composable(Screen.AuthForgotPassword.route) {
                    ForgotPasswordScreen(
                        viewModel = viewModel,
                        onNavigateToOtp = { navController.navigate(Screen.AuthOtp.route) },
                        onNavigateToLogin = { navController.navigate(Screen.AuthLogin.route) }
                    )
                }
                composable(Screen.AuthResetPassword.route) {
                    ResetPasswordScreen(
                        viewModel = viewModel,
                        onPasswordResetSuccess = { navController.navigate(Screen.AuthLogin.route) }
                    )
                }
                composable(Screen.AuthOtp.route) {
                    OtpVerificationScreen(
                        viewModel = viewModel,
                        onVerificationSuccess = { navController.navigate(Screen.AuthMfa.route) },
                        onResend = { viewModel.triggerOtpResend() }
                    )
                }
                composable(Screen.AuthMfa.route) {
                    MfaVerificationScreen(
                        viewModel = viewModel,
                        onMfaSuccess = { navController.navigate(Screen.AuthDeviceVerify.route) }
                    )
                }
                composable(Screen.AuthBiometric.route) {
                    BiometricAuthenticationScreen(
                        viewModel = viewModel,
                        onAuthSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.AuthBiometric.route) { inclusive = true }
                            }
                        },
                        onFallbackToPassword = { navController.navigate(Screen.AuthLogin.route) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.AuthFaceScan.route) {
                    FaceRecognitionScreen(
                        viewModel = viewModel,
                        onFallbackToPin = { navController.navigate(Screen.AuthBiometric.route) }
                    )
                }
                composable(Screen.AuthDeviceVerify.route) {
                    DeviceVerificationScreen(
                        viewModel = viewModel,
                        onDeviceApproved = { navController.navigate(Screen.AuthSecurityQuestions.route) }
                    )
                }
                composable(Screen.AuthSecurityQuestions.route) {
                    SecurityQuestionsScreen(
                        viewModel = viewModel,
                        onSuccess = { navController.navigate(Screen.Home.route) }
                    )
                }

                // --- USER DASHBOARD SUITE COMPOSABLES ---
                composable(Screen.DashboardHub.route) {
                    UserDashboardHubScreen(
                        viewModel = viewModel,
                        initialPage = UserDashboardPageType.GLOBAL,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DashboardGlobal.route) {
                    GlobalDashboardScreen(viewModel = viewModel)
                }
                composable(Screen.DashboardPersonal.route) {
                    PersonalDashboardScreen(viewModel = viewModel)
                }
                composable(Screen.DashboardHealth.route) {
                    FinancialHealthDashboardScreen(viewModel = viewModel)
                }
                composable(Screen.DashboardInsights.route) {
                    AiInsightsDashboardScreen(viewModel = viewModel)
                }
                composable(Screen.DashboardNotifications.route) {
                    NotificationsDashboardScreen(viewModel = viewModel)
                }
                composable(Screen.DashboardActivities.route) {
                    RecentActivitiesDashboardScreen(viewModel = viewModel)
                }
                composable(Screen.DashboardRecommendations.route) {
                    PersonalizedRecommendationsDashboardScreen(viewModel = viewModel)
                }

                // --- USER PROFILE SUITE (10 DISTINCT PAGES + HUB) ---
                composable(Screen.ProfileHub.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.PERSONAL_INFO,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfilePersonal.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.PERSONAL_INFO,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileDigitalId.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.DIGITAL_IDENTITY,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileDocuments.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.DOCUMENTS,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileEmployment.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.EMPLOYMENT,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileEducation.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.EDUCATION,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileFamily.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.FAMILY,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileAddress.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.ADDRESS,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileContact.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.CONTACT,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfileDevices.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.TRUSTED_DEVICES,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.ProfilePrivacy.route) {
                    UserProfileHubScreen(
                        viewModel = viewModel,
                        initialPage = UserProfilePageType.PRIVACY_SETTINGS,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }

                // --- DIGITAL IDENTITY SUITE (9 DISTINCT PAGES + HUB) ---
                composable(Screen.DidHub.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.UNIVERSAL_ID,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidUniversal.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.UNIVERSAL_ID,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidVerification.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.IDENTITY_VERIFICATION,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidSignature.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.DIGITAL_SIGNATURE,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidPassport.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.PASSPORT_VERIFY,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidDriverLicense.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.DRIVER_LICENSE,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidNationalId.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.NATIONAL_ID,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidKyc.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.KYC_VERIFICATION,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidFaceMatching.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.FACE_MATCHING,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.DidHistory.route) {
                    DigitalIdentityHubScreen(
                        viewModel = viewModel,
                        initialPage = DigitalIdentityPageType.IDENTITY_HISTORY,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }

                // --- BANKING MODULE SUITE (10 DISTINCT PAGES + HUB) ---
                composable(Screen.BankingHub.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.ACCOUNT_SUMMARY,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingSummary.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.ACCOUNT_SUMMARY,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingBalances.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.BALANCE_OVERVIEW,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingDeposit.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.DEPOSIT,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingWithdrawal.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.WITHDRAWAL,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingTransfer.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.MONEY_TRANSFER,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingInternational.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.INTERNATIONAL_TRANSFER,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingScheduled.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.SCHEDULED_PAYMENTS,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingStanding.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.STANDING_INSTRUCTIONS,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingBeneficiaries.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.BENEFICIARY_MANAGEMENT,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }
                composable(Screen.BankingStatements.route) {
                    BankingModuleHubScreen(
                        viewModel = viewModel,
                        initialPage = BankingModulePageType.ACCOUNT_STATEMENTS,
                        onNavigateToPage = { route -> navController.navigate(route) }
                    )
                }

                // ==========================================
                // MODULE 7: TRANSACTIONS (8 PAGES)
                // ==========================================
                composable(Screen.TxHub.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.HISTORY, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxHistory.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.HISTORY, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxDetails.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.DETAILS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxAnalytics.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.ANALYTICS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxSearch.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.SEARCH, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxExportPdf.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.EXPORT_PDF, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxExportCsv.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.EXPORT_CSV, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxCategories.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.CATEGORIES, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TxTimeline.route) {
                    TransactionModuleHubScreen(viewModel = viewModel, initialPage = TransactionPageType.SPENDING_TIMELINE, onNavigateToPage = { r -> navController.navigate(r) })
                }

                // ==========================================
                // MODULE 8: AI FINANCIAL DIGITAL TWIN (9 PAGES)
                // ==========================================
                composable(Screen.TwinHub.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinSimulation.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.SIMULATION, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinFutureWealth.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.FUTURE_WEALTH, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinRetirement.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.RETIREMENT, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinLoan.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.LOAN_SIM, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinEducation.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.EDUCATION_COST, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinFamily.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.FAMILY_PLANNING, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinEmergency.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.EMERGENCY_PLAN, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.TwinInvestment.route) {
                    AiDigitalTwinSuiteScreen(viewModel = viewModel, initialPage = DigitalTwinPageType.INVESTMENT_SIM, onNavigateToPage = { r -> navController.navigate(r) })
                }

                // ==========================================
                // MODULE 9: FINANCIAL INTELLIGENCE (8 PAGES)
                // ==========================================
                composable(Screen.FinIntelHub.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.INTEL_SCORE, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinIntelScore.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.INTEL_SCORE, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinHealthScore.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.HEALTH_SCORE, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinCreditHealth.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.CREDIT_HEALTH, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinDebtAnalysis.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.DEBT_ANALYSIS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinIncomeAnalysis.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.INCOME_ANALYSIS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinSpendingAnalysis.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.SPENDING_ANALYSIS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinWealthGrowth.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.WEALTH_GROWTH, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FinReports.route) {
                    FinancialIntelligenceSuiteScreen(viewModel = viewModel, initialPage = FinancialIntelPageType.FINANCIAL_REPORTS, onNavigateToPage = { r -> navController.navigate(r) })
                }

                // ==========================================
                // MODULE 10 & 11: WEALTH & BUDGET SUITE (15 PAGES)
                // ==========================================
                composable(Screen.WealthBudgetMasterHub.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.WEALTH_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthDashboardPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.WEALTH_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthSavingsPlanner.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.SAVINGS_PLANNER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthInvestmentPlanner.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.INVESTMENT_PLANNER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthRetirementPlanner.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.RETIREMENT_PLANNER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthTaxOpt.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.TAX_OPTIMIZATION, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthExpenseOpt.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.EXPENSE_OPTIMIZER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthPassiveIncome.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.PASSIVE_INCOME, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.WealthNetWorthCalc.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.NET_WORTH_CALC, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BudgetDashboardPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.BUDGET_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BudgetMonthlyPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.MONTHLY_BUDGET, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BudgetCategoriesPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.EXPENSE_CATEGORIES, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BudgetGoalsPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.GOAL_TRACKING, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BudgetSavingsGoalsPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.SAVINGS_GOALS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BudgetAlertsPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.SPENDING_ALERTS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BudgetRecommendationsPage.route) {
                    WealthAndBudgetSuiteScreen(viewModel = viewModel, initialPage = WealthBudgetPageType.SMART_RECOMMENDATIONS, onNavigateToPage = { r -> navController.navigate(r) })
                }

                // ==========================================
                // MODULE 12, 13, 14, 15: INVESTMENTS, LOANS, INSURANCE & COACH (22 PAGES)
                // ==========================================
                composable(Screen.InvestLoanMasterHub.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_PORTFOLIO, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestPortfolioPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_PORTFOLIO, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestStocksPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_STOCKS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestCryptoPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_CRYPTO, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestBondsPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_BONDS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestGoldPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_GOLD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestRealEstatePage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_REALESTATE, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestEsgPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_ESG, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InvestRiskPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INVEST_RISK, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.LoanDashboardPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.LOAN_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.LoanEligibilityPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.LOAN_ELIGIBILITY, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.LoanEmiCalcPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.LOAN_EMI_CALC, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.LoanAdvisorPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.LOAN_AI_ADVISOR, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.LoanRepaymentPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.LOAN_REPAYMENT, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InsuranceDashboardPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INSURANCE_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InsuranceHealthPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INSURANCE_HEALTH, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InsuranceVehiclePage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INSURANCE_VEHICLE, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InsurancePropertyPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INSURANCE_PROPERTY, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.InsuranceClaimsPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.INSURANCE_CLAIMS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CoachAssistantPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.COACH_ASSISTANT, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CoachInsightsPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.COACH_DAILY_INSIGHTS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CoachReportsPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.COACH_WEEKLY_REPORTS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CoachEducationPage.route) {
                    InvestmentLoanInsuranceSuiteScreen(viewModel = viewModel, initialPage = InvestLoanInsurancePageType.COACH_EDUCATION, onNavigateToPage = { r -> navController.navigate(r) })
                }

                // ==========================================
                // MODULE 16, 17, 31, 37: SECURITY, FRAUD, ADMIN & COMPLIANCE (17 PAGES)
                // ==========================================
                composable(Screen.SecurityFortressHub.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.FRAUD_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FraudDashboardPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.FRAUD_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FraudLivePage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.LIVE_MONITORING, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FraudSuspiciousPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.SUSPICIOUS_TX, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FraudPredictPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.FRAUD_PREDICTION, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CyberDashboardPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.CYBER_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CyberLoginsPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.LOGIN_HISTORY, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CyberDevicesPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.DEVICE_MGMT, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CyberThreatsPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.THREAT_DETECTION, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.AdminDashboardPage.route) {
                    AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) {
                        SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.ADMIN_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                    }
                }
                composable(Screen.AdminUserMgmtPage.route) {
                    AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) {
                        SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.USER_MGMT, onNavigateToPage = { r -> navController.navigate(r) })
                    }
                }
                composable(Screen.AdminRolesPage.route) {
                    AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) {
                        SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.ROLE_PERMISSIONS, onNavigateToPage = { r -> navController.navigate(r) })
                    }
                }
                composable(Screen.AdminAuditPage.route) {
                    AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) {
                        SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.AUDIT_LOGS, onNavigateToPage = { r -> navController.navigate(r) })
                    }
                }
                composable(Screen.AdminBackupPage.route) {
                    AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) {
                        SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.BACKUP_RECOVERY, onNavigateToPage = { r -> navController.navigate(r) })
                    }
                }
                composable(Screen.ComplianceDashboardPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.COMPLIANCE_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ComplianceAmlPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.AML_MONITORING, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ComplianceKycPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.KYC_MONITORING, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ComplianceReportsPage.route) {
                    SecurityFraudGovernanceSuiteScreen(viewModel = viewModel, initialPage = SecurityFraudPageType.REGULATORY_REPORTS, onNavigateToPage = { r -> navController.navigate(r) })
                }

                // ==========================================
                // MODULE 18, 19, 20, 21: MACRO, CLIMATE, HEALTH & EDUCATION (13 PAGES)
                // ==========================================
                composable(Screen.MacroClimateHealthMasterHub.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.GLOBAL_MACRO, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.MacroEconomyPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.GLOBAL_MACRO, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.MacroInflationPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.INFLATION_TRACKER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.MacroCommoditiesPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.COMMODITY_OIL, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.MacroRatesPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.INTEREST_RATES, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ClimateDashboardPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.CLIMATE_DASHBOARD, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ClimateCarbonScorePage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.CARBON_SCORE, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ClimateDisasterRiskPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.FLOOD_DISASTER_RISK, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.HealthExpensePlanPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.HEALTH_EXPENSE_PLAN, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.HospitalCostAiPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.HOSPITAL_PREDICTION, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.HsaVaultPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.HSA_SAVINGS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.EduPlannerCollegePage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.EDU_PLANNER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ScholarshipFinderPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.SCHOLARSHIP_FINDER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CareerPredictPage.route) {
                    MacroClimateHealthEducationSuiteScreen(viewModel = viewModel, initialPage = MacroClimateHealthEduPageType.CAREER_INCOME_PREDICT, onNavigateToPage = { r -> navController.navigate(r) })
                }

                // ==========================================
                // MODULE 22-30, 32-36, 38: ENTERPRISE, GOVT & FUTURE ECONOMY (18 PAGES)
                // ==========================================
                composable(Screen.EnterpriseFutureMasterHub.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.BUSINESS_HUB, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BusinessHubPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.BUSINESS_HUB, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BusinessPayrollPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.BUSINESS_PAYROLL, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.BusinessInvoicesPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.BUSINESS_INVOICES, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.SmartCityHubPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.SMART_CITY, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.CityUtilitiesPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.CITY_UTILITIES, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.GovtPortalPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.GOVERNMENT_PORTAL, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.DisasterReliefPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.DISASTER_MANAGEMENT, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.HumanitarianNgoPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.HUMANITARIAN_NGO, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.SustainabilityCenterPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.SUSTAINABILITY_CENTER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ResearchInnovationPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.RESEARCH_INNOVATION, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.AiMarketplacePage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.AI_MARKETPLACE, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.ExecutiveAnalyticsPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.EXECUTIVE_ANALYTICS, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.DeveloperCenterPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.DEVELOPER_CENTER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.FutureTechHubPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.FUTURE_TECH_HUB, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.SpaceLunarEconomyPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.SPACE_LUNAR_ECONOMY, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.NotificationCenterPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.NOTIFICATION_CENTER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.SettingsCenterPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.SETTINGS_CENTER, onNavigateToPage = { r -> navController.navigate(r) })
                }
                composable(Screen.HelpSupportCenterPage.route) {
                    EnterprisePublicFutureSuiteScreen(viewModel = viewModel, initialPage = EnterpriseFuturePageType.HELP_SUPPORT, onNavigateToPage = { r -> navController.navigate(r) })
                }
            }
        }
    }

    // Modal Dialogs
    if (isAuthDialogOpen) {
        AuthDialog(
            viewModel = viewModel,
            onDismiss = { isAuthDialogOpen = false }
        )
    }

    if (depositWithdrawState.isOpen) {
        DepositWithdrawDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.dismissDepositWithdrawDialog() }
        )
    }

    if (isNotificationsDialogOpen) {
        NotificationsDialog(
            viewModel = viewModel,
            onDismiss = { isNotificationsDialogOpen = false }
        )
    }

    if (isStatementDialogOpen) {
        StatementExportDialog(
            viewModel = viewModel,
            onDismiss = { isStatementDialogOpen = false }
        )
    }
}

