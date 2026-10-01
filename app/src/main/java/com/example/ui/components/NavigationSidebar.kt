package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Screen
import com.example.data.model.UserRole
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardLight
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.AuthUiState

data class NavCategoryItem(
    val screen: Screen,
    val subtitle: String,
    val tagColor: Color = CyberCyan,
    val badgeLabel: String? = null
)

@Composable
fun NavigationSidebarContent(
    currentRoute: String,
    authState: AuthUiState,
    onNavigate: (String) -> Unit,
    onOpenAuth: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = true,
    onToggleTheme: (() -> Unit)? = null
) {
    var searchQuery by remember { mutableStateOf("") }

    val publicPortalItems = remember {
        listOf(
            NavCategoryItem(Screen.PaidEdition, "Download Activation & Pro License", GoldAccent, "PAID / PRO"),
            NavCategoryItem(Screen.Landing, "Homepage & Value Proposition", CyberCyan, "PORTAL"),
            NavCategoryItem(Screen.AboutPfin, "Post-Quantum Architecture", CyberCyan),
            NavCategoryItem(Screen.VisionMission, "2050–2077 Planetary Vision", CyberCyan),
            NavCategoryItem(Screen.FeaturesOverview, "Deep Feature Index & Matrix", CyberCyan),
            NavCategoryItem(Screen.Solutions, "Enterprise & Sovereign Nodes", ElectricBlue),
            NavCategoryItem(Screen.Industries, "Health, Climate, Space & Cities", ElectricBlue),
            NavCategoryItem(Screen.Pricing, "Tiers & Dynamic SLA Estimator", GoldAccent),
            NavCategoryItem(Screen.SuccessStories, "Case Studies & Metrics", EmeraldSuccess),
            NavCategoryItem(Screen.Blog, "Future Insights & Articles", PurpleTech),
            NavCategoryItem(Screen.ResearchCenter, "ZK & NIST Cryptographic Lab", CyberCyan, "R&D"),
            NavCategoryItem(Screen.ContactUs, "Sovereign Dispatch & Mesh Nodes", AmberOrange),
            NavCategoryItem(Screen.Careers, "Quantum & AI Open Roles", EmeraldSuccess, "HIRING"),
            NavCategoryItem(Screen.Faqs, "Protocol & Operations Accordion", CyberCyan),
            NavCategoryItem(Screen.Terms, "Algorithmic Smart Covenants", TextMuted),
            NavCategoryItem(Screen.Privacy, "Zero-Knowledge Biometric Sovereignty", TextMuted)
        )
    }

    val futureOsItems = remember {
        listOf(
            NavCategoryItem(Screen.FutureOs, "20-Pillar Master Operations Hub", GoldAccent, "MASTER"),
            NavCategoryItem(Screen.Advisor, "Pillar 1: AGI Lifelong Partner", PurpleTech, "AGI"),
            NavCategoryItem(Screen.GlobalEconomy, "Pillar 2: Macro & War Predictor", GoldAccent, "MACRO"),
            NavCategoryItem(Screen.WorldIdentity, "Pillar 3: Unified Sovereign ID", CyberCyan, "ID"),
            NavCategoryItem(Screen.LifePlanner, "Pillar 4: 7-Stage Life Roadmap", PurpleTech, "LIFECYCLE"),
            NavCategoryItem(Screen.WealthEngine, "Pillar 5: Universal Autopilot", GoldAccent, "AUTO"),
            NavCategoryItem(Screen.CrisisIntelligence, "Pillar 6: Planetary Crisis AI", AmberOrange, "CRISIS"),
            NavCategoryItem(Screen.PillarsPart1, "Pillars 7–11: Eco, Cities & Climate", EmeraldSuccess),
            NavCategoryItem(Screen.PillarsPart2, "Pillars 12–16: Space & Quantum", ElectricBlue),
            NavCategoryItem(Screen.PillarsPart3, "Pillars 17–20: Mesh & Governance", GoldAccent)
        )
    }

    val bankingOpsItems = remember {
        listOf(
            NavCategoryItem(Screen.Home, "Consolidated Vault & Audit", CyberCyan, "DASHBOARD"),
            NavCategoryItem(Screen.Transfer, "Instant Multi-Rail Dispatch", CyberCyan),
            NavCategoryItem(Screen.Security, "Zero-Trust Threat Radar", EmeraldSuccess, "SECURE"),
            NavCategoryItem(Screen.Analytics, "Cash Flow & Forecasting", ElectricBlue),
            NavCategoryItem(Screen.Loans, "Microcredit & Smart EMI", GoldAccent),
            NavCategoryItem(Screen.Investments, "Global Assets & Staking", GoldAccent),
            NavCategoryItem(Screen.Insurance, "Parametric Smart Policies", PurpleTech),
            NavCategoryItem(Screen.Applications, "Fast Onboarding & KYC Hub", CyberCyan, "APPLY"),
            NavCategoryItem(Screen.Bills, "Autonomous Bill Pay", EmeraldSuccess),
            NavCategoryItem(Screen.Literacy, "Financial Simulator Academy", CyberCyan),
            NavCategoryItem(Screen.Admin, "Sovereign Node Controls", AmberOrange, "ADMIN")
        )
    }

    val authSuiteItems = remember {
        listOf(
            NavCategoryItem(Screen.AuthHub, "10-Page Master Auth Suite", CyberCyan, "PORTAL"),
            NavCategoryItem(Screen.AuthLogin, "Master Account Sign In", CyberCyan, "LOGIN"),
            NavCategoryItem(Screen.AuthRegister, "New Vault Registration", ElectricBlue, "SIGNUP"),
            NavCategoryItem(Screen.AuthForgotPassword, "Multi-Channel Recovery", AmberOrange),
            NavCategoryItem(Screen.AuthResetPassword, "High-Entropy Vault Key", PurpleTech),
            NavCategoryItem(Screen.AuthOtp, "6-Digit SMS/Email Token", EmeraldSuccess, "OTP"),
            NavCategoryItem(Screen.AuthMfa, "TOTP & Hardware Security Key", GoldAccent, "2FA"),
            NavCategoryItem(Screen.AuthBiometric, "Enclave Fingerprint Scan", CyberCyan, "BIO"),
            NavCategoryItem(Screen.AuthFaceScan, "AI 3D Contour Face ID", PurpleTech, "AI"),
            NavCategoryItem(Screen.AuthDeviceVerify, "Hardware Binding & Geofence", ElectricBlue),
            NavCategoryItem(Screen.AuthSecurityQuestions, "AES-256 Recovery Vault", AmberOrange)
        )
    }

    val dashboardSuiteItems = remember {
        listOf(
            NavCategoryItem(Screen.DashboardHub, "Master 360° Switcher", CyberCyan, "HUB"),
            NavCategoryItem(Screen.DashboardGlobal, "Planetary Multi-Region Grid", CyberCyan, "GLOBAL"),
            NavCategoryItem(Screen.DashboardPersonal, "Sovereign Vaults & Goals", ElectricBlue, "PERSONAL"),
            NavCategoryItem(Screen.DashboardHealth, "Diagnostic 1000pt Score", EmeraldSuccess, "HEALTH"),
            NavCategoryItem(Screen.DashboardInsights, "Predictive Anomaly Engine", PurpleTech, "AI"),
            NavCategoryItem(Screen.DashboardNotifications, "Real-Time Telemetry Stream", AmberOrange, "ALERTS"),
            NavCategoryItem(Screen.DashboardActivities, "Signed Transaction Ledger", CyberCyan, "AUDIT"),
            NavCategoryItem(Screen.DashboardRecommendations, "Personalized Yield Directives", GoldAccent, "DIRECTIVES")
        )
    }

    val userProfileItems = remember {
        listOf(
            NavCategoryItem(Screen.ProfileHub, "Master 10-Page Profile Hub", CyberCyan, "HUB"),
            NavCategoryItem(Screen.ProfilePersonal, "Legal Name, DOB & Tax TIN", CyberCyan, "LEGAL"),
            NavCategoryItem(Screen.ProfileDigitalId, "W3C DID & zk-Attestations", PurpleTech, "DID"),
            NavCategoryItem(Screen.ProfileDocuments, "AES-256 KYC & ID Vault", EmeraldSuccess, "DOCS"),
            NavCategoryItem(Screen.ProfileEmployment, "Corporate Payroll & Income", ElectricBlue, "INCOME"),
            NavCategoryItem(Screen.ProfileEducation, "Degrees & Academic zk-Proofs", GoldAccent, "EDU"),
            NavCategoryItem(Screen.ProfileFamily, "Beneficiaries & Kin Contacts", AmberOrange, "KIN"),
            NavCategoryItem(Screen.ProfileAddress, "Residential & Tax Jurisdictions", CyberCyan, "GEO"),
            NavCategoryItem(Screen.ProfileContact, "PGP Signed Channels & Matrix", ElectricBlue, "PGP"),
            NavCategoryItem(Screen.ProfileDevices, "FIDO2 Enclaves & Hardware Keys", PurpleTech, "FIDO2"),
            NavCategoryItem(Screen.ProfilePrivacy, "GDPR & Zero-Knowledge Disclosures", CrimsonDanger, "GDPR")
        )
    }

    val digitalIdentityItems = remember {
        listOf(
            NavCategoryItem(Screen.DidHub, "Master 9-Page Digital Identity Hub", CyberCyan, "HUB"),
            NavCategoryItem(Screen.DidUniversal, "Universal W3C DID & Root Enclave", CyberCyan, "DID"),
            NavCategoryItem(Screen.DidVerification, "Identity Clearance & Audit Tiers", EmeraldSuccess, "VERIFY"),
            NavCategoryItem(Screen.DidSignature, "Post-Quantum Dilithium Signing", PurpleTech, "SIGN"),
            NavCategoryItem(Screen.DidPassport, "ICAO 9303 ePassport MRZ & PKD", GoldAccent, "PASSPORT"),
            NavCategoryItem(Screen.DidDriverLicense, "AAMVA DMV RealID Gold Star", ElectricBlue, "DMV"),
            NavCategoryItem(Screen.DidNationalId, "National ID & zk-SSN Token", CyberCyan, "NAT-ID"),
            NavCategoryItem(Screen.DidKyc, "OFAC Sanctions & AML Screening", AmberOrange, "AML"),
            NavCategoryItem(Screen.DidFaceMatching, "3D Liveness & 128-Vector Match", EmeraldSuccess, "3D-FACE"),
            NavCategoryItem(Screen.DidHistory, "Immutable Audit Trail Ledger", PurpleTech, "AUDIT")
        )
    }

    val bankingModuleSuiteItems = remember {
        listOf(
            NavCategoryItem(Screen.BankingHub, "Master 10-Page Banking Hub", CyberCyan, "HUB"),
            NavCategoryItem(Screen.BankingSummary, "Account Ledger & IBANs", CyberCyan, "ACC"),
            NavCategoryItem(Screen.BankingBalances, "Multi-Currency Spot & Yield", EmeraldSuccess, "FX"),
            NavCategoryItem(Screen.BankingDeposit, "Instant Inflow & Check OCR", CyberCyan, "INFLOW"),
            NavCategoryItem(Screen.BankingWithdrawal, "Outflow Wire & Enclave 2FA", AmberOrange, "OUTFLOW"),
            NavCategoryItem(Screen.BankingTransfer, "Domestic FedNow / RTP Rails", ElectricBlue, "SEND"),
            NavCategoryItem(Screen.BankingInternational, "SWIFT & Cross-Border FX", PurpleTech, "SWIFT"),
            NavCategoryItem(Screen.BankingScheduled, "Recurring Auto-Debit Calendar", GoldAccent, "SCHEDULE"),
            NavCategoryItem(Screen.BankingStanding, "Smart Sweep Treasury Rules", CyberCyan, "RULES"),
            NavCategoryItem(Screen.BankingBeneficiaries, "Attested Payee Whitelist", ElectricBlue, "PAYEES"),
            NavCategoryItem(Screen.BankingStatements, "Signed PDF / XML Tax Packs", EmeraldSuccess, "DOCS")
        )
    }

    val transactionSuiteItems = remember {
        listOf(
            NavCategoryItem(Screen.TxHub, "Transactions Master Hub", CyberCyan, "HUB"),
            NavCategoryItem(Screen.TxHistory, "Real-time Multi-Rail Ledger", CyberCyan, "HISTORY"),
            NavCategoryItem(Screen.TxDetails, "Cryptographic Proof & Invoices", ElectricBlue, "RECEIPT"),
            NavCategoryItem(Screen.TxAnalytics, "Category & Merchant Breakdown", EmeraldSuccess, "ANALYTICS"),
            NavCategoryItem(Screen.TxSearch, "Natural Language & Regex Query", CyberCyan, "SEARCH"),
            NavCategoryItem(Screen.TxExportPdf, "Signed PDF Statements", GoldAccent, "PDF"),
            NavCategoryItem(Screen.TxExportCsv, "Raw Ledger CSV / JSON Dump", PurpleTech, "CSV"),
            NavCategoryItem(Screen.TxCategories, "Autonomous Categorization Rules", AmberOrange, "RULES"),
            NavCategoryItem(Screen.TxTimeline, "Chronological Cashflow Velocity", ElectricBlue, "TIMELINE")
        )
    }

    val digitalTwinSuiteItems = remember {
        listOf(
            NavCategoryItem(Screen.TwinHub, "Digital Twin Simulation Hub", PurpleTech, "HUB"),
            NavCategoryItem(Screen.TwinSimulation, "Monte Carlo Stochastic Model", CyberCyan, "MONTE"),
            NavCategoryItem(Screen.TwinFutureWealth, "10–30 Year Wealth Forecast", EmeraldSuccess, "WEALTH"),
            NavCategoryItem(Screen.TwinRetirement, "FIRE & Safe Withdrawal (SWR)", GoldAccent, "FIRE"),
            NavCategoryItem(Screen.TwinLoan, "Amortization & Refi Optimizer", ElectricBlue, "LOAN"),
            NavCategoryItem(Screen.TwinEducation, "529 & Ivy Tuition Inflation", PurpleTech, "EDU"),
            NavCategoryItem(Screen.TwinFamily, "Childcare & Real Estate Shift", AmberOrange, "FAMILY"),
            NavCategoryItem(Screen.TwinEmergency, "Job Loss & Macro Stress Test", CrimsonDanger, "CRISIS"),
            NavCategoryItem(Screen.TwinInvestment, "Alpha Strategy Backtester", EmeraldSuccess, "ALPHA")
        )
    }

    val financialIntelSuiteItems = remember {
        listOf(
            NavCategoryItem(Screen.FinIntelHub, "Financial Intelligence Hub", CyberCyan, "HUB"),
            NavCategoryItem(Screen.FinIntelScore, "PFIN Global Financial IQ (885)", EmeraldSuccess, "IQ"),
            NavCategoryItem(Screen.FinHealthScore, "1000-Point Health Diagnostic", CyberCyan, "HEALTH"),
            NavCategoryItem(Screen.FinCreditHealth, "Decentralized FICO 815 Score", ElectricBlue, "CREDIT"),
            NavCategoryItem(Screen.FinDebtAnalysis, "DTI Snowball Payoff Plan", AmberOrange, "DEBT"),
            NavCategoryItem(Screen.FinIncomeAnalysis, "Multi-Stream Inflow Dynamics", GoldAccent, "INCOME"),
            NavCategoryItem(Screen.FinSpendingAnalysis, "30-Day Velocity & Heatmap", PurpleTech, "SPEND"),
            NavCategoryItem(Screen.FinWealthGrowth, "CAGR & S&P Alpha Spread", EmeraldSuccess, "CAGR"),
            NavCategoryItem(Screen.FinReports, "Automated Certified AI Reports", CyberCyan, "REPORTS")
        )
    }

    val wealthBudgetSuiteItems = remember {
        listOf(
            NavCategoryItem(Screen.WealthBudgetMasterHub, "Wealth & Budget Master Hub", GoldAccent, "HUB"),
            NavCategoryItem(Screen.WealthDashboardPage, "Consolidated Net Worth Engine", GoldAccent, "WEALTH"),
            NavCategoryItem(Screen.WealthSavingsPlanner, "Autonomous High-Yield Savings", EmeraldSuccess, "SAVE"),
            NavCategoryItem(Screen.WealthInvestmentPlanner, "Automated DCA Asset Allocation", ElectricBlue, "INVEST"),
            NavCategoryItem(Screen.WealthRetirementPlanner, "Mega-Backdoor Roth & 401(k)", GoldAccent, "RETIRE"),
            NavCategoryItem(Screen.WealthTaxOpt, "Tax-Loss Harvesting & Shelters", CyberCyan, "TAX"),
            NavCategoryItem(Screen.WealthExpenseOpt, "Recurring Subscriptions Audit", AmberOrange, "EXPENSE"),
            NavCategoryItem(Screen.WealthPassiveIncome, "Dividends & Staking Cashflow", EmeraldSuccess, "PASSIVE"),
            NavCategoryItem(Screen.WealthNetWorthCalc, "Interactive Asset / Debt Calc", PurpleTech, "CALC"),
            NavCategoryItem(Screen.BudgetDashboardPage, "50/30/20 Rule Health Check", CyberCyan, "BUDGET"),
            NavCategoryItem(Screen.BudgetMonthlyPage, "Monthly Category Allocations", ElectricBlue, "MONTHLY"),
            NavCategoryItem(Screen.BudgetCategoriesPage, "Autonomous Caps & Alerts", AmberOrange, "CAPS"),
            NavCategoryItem(Screen.BudgetGoalsPage, "Strategic Financial Target Vaults", GoldAccent, "GOALS"),
            NavCategoryItem(Screen.BudgetSavingsGoalsPage, "Dedicated Emergency & Vacation", EmeraldSuccess, "TARGETS"),
            NavCategoryItem(Screen.BudgetAlertsPage, "Real-time Velocity Thresholds", CrimsonDanger, "ALERTS"),
            NavCategoryItem(Screen.BudgetRecommendationsPage, "Autonomous AI Rebalancing", CyberCyan, "AI-TIPS")
        )
    }

    val investLoanInsuranceItems = remember {
        listOf(
            NavCategoryItem(Screen.InvestLoanMasterHub, "Invest, Loan & Insurance Hub", EmeraldSuccess, "HUB"),
            NavCategoryItem(Screen.InvestPortfolioPage, "Global Portfolio ($485,250)", EmeraldSuccess, "PORTFOLIO"),
            NavCategoryItem(Screen.InvestStocksPage, "Mega-Cap Tech Equities", CyberCyan, "STOCKS"),
            NavCategoryItem(Screen.InvestCryptoPage, "Bluechip Digital Assets", PurpleTech, "CRYPTO"),
            NavCategoryItem(Screen.InvestBondsPage, "US Treasury Yield Curve", ElectricBlue, "BONDS"),
            NavCategoryItem(Screen.InvestGoldPage, "Physical Swiss Vault Gold", GoldAccent, "GOLD"),
            NavCategoryItem(Screen.InvestRealEstatePage, "Fractional Commercial Syndicates", CyberCyan, "REIT"),
            NavCategoryItem(Screen.InvestEsgPage, "Carbon Offsets & Clean Tech", EmeraldSuccess, "ESG"),
            NavCategoryItem(Screen.InvestRiskPage, "Value at Risk (VaR) Analysis", AmberOrange, "RISK"),
            NavCategoryItem(Screen.LoanDashboardPage, "Active Mortgages & Credit Lines", CyberCyan, "LOANS"),
            NavCategoryItem(Screen.LoanEligibilityPage, "Pre-Approved Sovereign Capacity", EmeraldSuccess, "ELIGIBLE"),
            NavCategoryItem(Screen.LoanEmiCalcPage, "Interactive EMI & Amortization", GoldAccent, "EMI"),
            NavCategoryItem(Screen.LoanAdvisorPage, "Refinance & Rate Cut Advisor", PurpleTech, "ADVISOR"),
            NavCategoryItem(Screen.LoanRepaymentPage, "Accelerated Snowball Payoff", ElectricBlue, "PAYOFF"),
            NavCategoryItem(Screen.InsuranceDashboardPage, "Umbrella Policy Cover ($2.5M)", EmeraldSuccess, "INSURE"),
            NavCategoryItem(Screen.InsuranceHealthPage, "Platinum Worldwide Medical", CyberCyan, "HEALTH"),
            NavCategoryItem(Screen.InsuranceVehiclePage, "Autonomous EV Telematics", ElectricBlue, "VEHICLE"),
            NavCategoryItem(Screen.InsurancePropertyPage, "Real Estate Hazard Cover", GoldAccent, "PROPERTY"),
            NavCategoryItem(Screen.InsuranceClaimsPage, "Instant Smart Contract Claim", PurpleTech, "CLAIMS"),
            NavCategoryItem(Screen.CoachAssistantPage, "24/7 Personal AI Strategist", CyberCyan, "COACH"),
            NavCategoryItem(Screen.CoachInsightsPage, "Daily Financial Actionables", EmeraldSuccess, "DAILY"),
            NavCategoryItem(Screen.CoachReportsPage, "Weekly Wealth Velocity Digest", GoldAccent, "WEEKLY"),
            NavCategoryItem(Screen.CoachEducationPage, "Academy: Advanced Arbitrage", PurpleTech, "ACADEMY")
        )
    }

    val securityGovernanceItems = remember {
        listOf(
            NavCategoryItem(Screen.SecurityFortressHub, "Zero-Trust Fortress Hub", CrimsonDanger, "HUB"),
            NavCategoryItem(Screen.FraudDashboardPage, "Autonomous Fraud Interceptor", CrimsonDanger, "FRAUD"),
            NavCategoryItem(Screen.FraudLivePage, "Live Threat & Velocity Feed", AmberOrange, "LIVE"),
            NavCategoryItem(Screen.FraudSuspiciousPage, "Suspicious Activity SAR Queue", CrimsonDanger, "SAR"),
            NavCategoryItem(Screen.FraudPredictPage, "Graph Network Risk Scoring", CyberCyan, "PREDICT"),
            NavCategoryItem(Screen.CyberDashboardPage, "Post-Quantum Kyber-1024", PurpleTech, "QUANTUM"),
            NavCategoryItem(Screen.CyberLoginsPage, "Attested Sessions & History", CyberCyan, "SESSIONS"),
            NavCategoryItem(Screen.CyberDevicesPage, "YubiKey & StrongBox Tokens", EmeraldSuccess, "FIDO2"),
            NavCategoryItem(Screen.CyberThreatsPage, "Memory & Malware Attestation", ElectricBlue, "SHIELD"),
            NavCategoryItem(Screen.AdminDashboardPage, "Sovereign Root Administrator", PurpleTech, "ADMIN"),
            NavCategoryItem(Screen.AdminUserMgmtPage, "Enterprise User Directory", CyberCyan, "USERS"),
            NavCategoryItem(Screen.AdminRolesPage, "Multi-Sig RBAC Privileges", GoldAccent, "ROLES"),
            NavCategoryItem(Screen.AdminAuditPage, "Immutable Cryptographic Audit", TextWhite, "AUDIT"),
            NavCategoryItem(Screen.AdminBackupPage, "Shamir 3-of-5 Secret Recovery", EmeraldSuccess, "BACKUP"),
            NavCategoryItem(Screen.ComplianceDashboardPage, "Basel III & ISO 20022 Ratios", EmeraldSuccess, "BASEL"),
            NavCategoryItem(Screen.ComplianceAmlPage, "OFAC & PEP Sanction Screening", CrimsonDanger, "AML"),
            NavCategoryItem(Screen.ComplianceKycPage, "Zero-Knowledge Sovereign Tiers", CyberCyan, "KYC"),
            NavCategoryItem(Screen.ComplianceReportsPage, "FinCEN & MiCA Regulatory Pack", GoldAccent, "REPORTS")
        )
    }

    val macroClimateItems = remember {
        listOf(
            NavCategoryItem(Screen.MacroClimateHealthMasterHub, "Macro & Planetary Hub", CyberCyan, "HUB"),
            NavCategoryItem(Screen.MacroEconomyPage, "Global GDP & Caixin PMI", CyberCyan, "MACRO"),
            NavCategoryItem(Screen.MacroInflationPage, "US Headline & Core CPI Radar", AmberOrange, "CPI"),
            NavCategoryItem(Screen.MacroCommoditiesPage, "Brent Crude, Gas & Lithium", GoldAccent, "COMMODITY"),
            NavCategoryItem(Screen.MacroRatesPage, "Fed, ECB & BoJ Policy Curve", ElectricBlue, "RATES"),
            NavCategoryItem(Screen.ClimateDashboardPage, "Planetary Carbon Credit Registry", EmeraldSuccess, "CLIMATE"),
            NavCategoryItem(Screen.ClimateCarbonScorePage, "Spending Carbon Diagnostic", EmeraldSuccess, "CARBON"),
            NavCategoryItem(Screen.ClimateDisasterRiskPage, "Geospatial Flood Risk Model", AmberOrange, "FLOOD"),
            NavCategoryItem(Screen.HealthExpensePlanPage, "Family Healthcare Expense Plan", CyberCyan, "MEDICAL"),
            NavCategoryItem(Screen.HospitalCostAiPage, "In-Network Cost Estimator", ElectricBlue, "HOSPITAL"),
            NavCategoryItem(Screen.HsaVaultPage, "Invested Stealth HSA IRA", EmeraldSuccess, "HSA"),
            NavCategoryItem(Screen.EduPlannerCollegePage, "529 College Tuition Ladder", PurpleTech, "529-PLAN"),
            NavCategoryItem(Screen.ScholarshipFinderPage, "AI Academic Grant Matcher", GoldAccent, "SCHOLAR"),
            NavCategoryItem(Screen.CareerPredictPage, "Staff Architect Comp Trajectory", EmeraldSuccess, "SALARY")
        )
    }

    val enterpriseFutureItems = remember {
        listOf(
            NavCategoryItem(Screen.EnterpriseFutureMasterHub, "Enterprise & Future Hub", PurpleTech, "HUB"),
            NavCategoryItem(Screen.BusinessHubPage, "Commercial Treasury ($1.8M)", CyberCyan, "TREASURY"),
            NavCategoryItem(Screen.BusinessPayrollPage, "Multi-Jurisdiction Global Payroll", EmeraldSuccess, "PAYROLL"),
            NavCategoryItem(Screen.BusinessInvoicesPage, "Autonomous Accounts Payable", ElectricBlue, "AP/AR"),
            NavCategoryItem(Screen.SmartCityHubPage, "Municipal EV & Property Taxes", CyberCyan, "SMART-CITY"),
            NavCategoryItem(Screen.CityUtilitiesPage, "Smart Meter Net Solar Grid", EmeraldSuccess, "GRID"),
            NavCategoryItem(Screen.GovtPortalPage, "Sovereign Digital Identity Gateway", GoldAccent, "GOV"),
            NavCategoryItem(Screen.DisasterReliefPage, "Instant Biometric Relief Aid", CrimsonDanger, "RELIEF"),
            NavCategoryItem(Screen.HumanitarianNgoPage, "On-Chain 501(c)(3) Donations", EmeraldSuccess, "AID"),
            NavCategoryItem(Screen.SustainabilityCenterPage, "Corporate AAA ESG Rating", EmeraldSuccess, "ESG"),
            NavCategoryItem(Screen.ResearchInnovationPage, "Quantum ZKP FinTech Lab", PurpleTech, "R&D"),
            NavCategoryItem(Screen.AiMarketplacePage, "AI Plugin & Algorithmic Store", CyberCyan, "STORE"),
            NavCategoryItem(Screen.ExecutiveAnalyticsPage, "Executive Board LTV / CAC KPIs", GoldAccent, "KPIS"),
            NavCategoryItem(Screen.DeveloperCenterPage, "REST & Webhook API Keys", CyberCyan, "DEV-API"),
            NavCategoryItem(Screen.FutureTechHubPage, "FedNow CBDC & IoT Micropayments", PurpleTech, "CBDC"),
            NavCategoryItem(Screen.SpaceLunarEconomyPage, "Orbital & Lunar Lagrangian Bonds", GoldAccent, "SPACE"),
            NavCategoryItem(Screen.NotificationCenterPage, "Push & Priority Security Feed", CyberCyan, "ALERTS"),
            NavCategoryItem(Screen.SettingsCenterPage, "System Preferences & Enclaves", TextWhite, "SETTINGS"),
            NavCategoryItem(Screen.HelpSupportCenterPage, "24/7 Dedicated AI Concierge", EmeraldSuccess, "SUPPORT")
        )
    }

    val filteredPublic = remember(searchQuery) {
        if (searchQuery.isBlank()) publicPortalItems
        else publicPortalItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredFuture = remember(searchQuery) {
        if (searchQuery.isBlank()) futureOsItems
        else futureOsItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredBanking = remember(searchQuery) {
        if (searchQuery.isBlank()) bankingOpsItems
        else bankingOpsItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredAuth = remember(searchQuery) {
        if (searchQuery.isBlank()) authSuiteItems
        else authSuiteItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredDashboards = remember(searchQuery) {
        if (searchQuery.isBlank()) dashboardSuiteItems
        else dashboardSuiteItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredProfiles = remember(searchQuery) {
        if (searchQuery.isBlank()) userProfileItems
        else userProfileItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredDid = remember(searchQuery) {
        if (searchQuery.isBlank()) digitalIdentityItems
        else digitalIdentityItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredBankingSuite = remember(searchQuery) {
        if (searchQuery.isBlank()) bankingModuleSuiteItems
        else bankingModuleSuiteItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredTransactions = remember(searchQuery) {
        if (searchQuery.isBlank()) transactionSuiteItems
        else transactionSuiteItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredDigitalTwin = remember(searchQuery) {
        if (searchQuery.isBlank()) digitalTwinSuiteItems
        else digitalTwinSuiteItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredFinancialIntel = remember(searchQuery) {
        if (searchQuery.isBlank()) financialIntelSuiteItems
        else financialIntelSuiteItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredWealthBudget = remember(searchQuery) {
        if (searchQuery.isBlank()) wealthBudgetSuiteItems
        else wealthBudgetSuiteItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredInvestLoan = remember(searchQuery) {
        if (searchQuery.isBlank()) investLoanInsuranceItems
        else investLoanInsuranceItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredSecurityGov = remember(searchQuery) {
        if (searchQuery.isBlank()) securityGovernanceItems
        else securityGovernanceItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredMacroClimate = remember(searchQuery) {
        if (searchQuery.isBlank()) macroClimateItems
        else macroClimateItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    val filteredEnterpriseFuture = remember(searchQuery) {
        if (searchQuery.isBlank()) enterpriseFutureItems
        else enterpriseFutureItems.filter { it.screen.title.contains(searchQuery, true) || it.subtitle.contains(searchQuery, true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        // --- 1. Header with Quantum Logo & Live Node Telemetry ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.2f))
                            .border(1.5.dp, CyberCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "PFIN Logo",
                            tint = CyberCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("PFIN GLOBAL OS", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 15.sp)
                            PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                        }
                        Text("Planetary Financial Intelligence 2077", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Navy800,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.5.dp, CyberCyan.copy(alpha = 0.3f))
                    ) {
                        Text(
                            "ZK-SHIELD ACTIVE • 1.2ms",
                            color = CyberCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        color = EmeraldSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "NODES: 10,480 SYNCED",
                            color = EmeraldSuccess,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        // --- 2. Instant Search Filter Bar ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search 35+ pages & modules...", color = TextMuted, fontSize = 11.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = CyberCyan, modifier = Modifier.size(18.dp)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberCyan,
                unfocusedBorderColor = Navy700,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedContainerColor = Navy800,
                unfocusedContainerColor = Navy800
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .height(48.dp)
                .testTag("sidebar_search_input")
        )

        // --- VIP Pro / Paid Edition Quick CTA Card ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .clickable { onNavigate(Screen.PaidEdition.route) }
                .testTag("sidebar_vip_pro_banner"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.7f))
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(GoldAccent.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Pro Pass",
                        tint = GoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("PFIN PRO & PAID EDITION", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                        Surface(color = GoldAccent, shape = RoundedCornerShape(4.dp)) {
                            Text("VIP", color = Navy900, fontSize = 8.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                        }
                    }
                    Text("Download License & Unlimited AI Rails", color = CyberCyan, fontSize = 9.sp)
                }

                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
            }
        }

        // --- Theme Toggle Controller ---
        onToggleTheme?.let { toggle ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { toggle() }
                    .testTag("sidebar_theme_toggle"),
                color = if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDarkMode) Navy700 else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = if (isDarkMode) AmberOrange else CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = if (isDarkMode) "Dark Theme Active" else "Light Theme Active",
                                color = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap or toggle to switch",
                                color = if (isDarkMode) TextMuted else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { toggle() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberCyan,
                            checkedTrackColor = Navy900,
                            uncheckedThumbColor = AmberOrange,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.testTag("sidebar_theme_switch")
                    )
                }
            }
        }

        // --- 3. Scrollable Categorized Nav Items ---
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // SECTION 1: PUBLIC PORTAL PAGES
            if (filteredPublic.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🌐 PUBLIC PORTAL PAGES",
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredPublic.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredPublic) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 2: NEXT-GEN FINANCIAL OS (20 PILLARS)
            if (filteredFuture.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🚀 NEXT-GEN FINANCIAL OS (2050–2077)",
                            color = GoldAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredFuture.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredFuture) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 3: BANKING OPERATIONS
            if (filteredBanking.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "💳 BANKING & OPERATIONS",
                            color = EmeraldSuccess,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredBanking.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredBanking) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 4: AUTHENTICATION & ZERO-TRUST SECURITY
            if (filteredAuth.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🔐 AUTHENTICATION & SECURITY",
                            color = AmberOrange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredAuth.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredAuth) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 5: 360° USER DASHBOARD SUITE
            if (filteredDashboards.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "📊 USER DASHBOARDS (360°)",
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredDashboards.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredDashboards) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 6: USER PROFILE SUITE (10 PAGES)
            if (filteredProfiles.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "👤 USER PROFILE SUITE (10 PAGES)",
                            color = PurpleTech,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredProfiles.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredProfiles) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 7: DIGITAL IDENTITY SUITE (9 PAGES)
            if (filteredDid.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🌐 DIGITAL IDENTITY (9 PAGES)",
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredDid.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredDid) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 8: BANKING MODULE SUITE (10 PAGES)
            if (filteredBankingSuite.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🏦 BANKING MODULE (10 PAGES)",
                            color = EmeraldSuccess,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredBankingSuite.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                items(filteredBankingSuite) { item ->
                    SidebarItemRow(
                        item = item,
                        isSelected = currentRoute == item.screen.route,
                        onClick = { onNavigate(item.screen.route) }
                    )
                }
            }

            // SECTION 9: TRANSACTIONS MODULE (8 PAGES)
            if (filteredTransactions.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🧾 TRANSACTIONS SUITE (8 PAGES)",
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredTransactions.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredTransactions) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }

            // SECTION 10: AI FINANCIAL DIGITAL TWIN (9 PAGES)
            if (filteredDigitalTwin.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🧠 AI DIGITAL TWIN (9 PAGES)",
                            color = PurpleTech,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredDigitalTwin.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredDigitalTwin) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }

            // SECTION 11: FINANCIAL INTELLIGENCE (8 PAGES)
            if (filteredFinancialIntel.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "📈 FINANCIAL INTELLIGENCE (8 PAGES)",
                            color = EmeraldSuccess,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredFinancialIntel.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredFinancialIntel) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }

            // SECTION 12: WEALTH & BUDGET MANAGEMENT (15 PAGES)
            if (filteredWealthBudget.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "💰 WEALTH & BUDGET (15 PAGES)",
                            color = GoldAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredWealthBudget.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredWealthBudget) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }

            // SECTION 13: INVESTMENTS, LOANS, INSURANCE & COACH (22 PAGES)
            if (filteredInvestLoan.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🚀 INVEST, LOANS & COACH (22 PAGES)",
                            color = EmeraldSuccess,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredInvestLoan.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredInvestLoan) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }

            // SECTION 14: SECURITY, FRAUD, ADMIN & COMPLIANCE (17 PAGES)
            if (filteredSecurityGov.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🛡️ ZERO-TRUST FORTRESS (17 PAGES)",
                            color = CrimsonDanger,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredSecurityGov.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredSecurityGov) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }

            // SECTION 15: MACRO, CLIMATE, HEALTH & EDUCATION (13 PAGES)
            if (filteredMacroClimate.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🌍 MACRO & PLANETARY (13 PAGES)",
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredMacroClimate.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredMacroClimate) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }

            // SECTION 16: ENTERPRISE, GOVT & FUTURE ECONOMY (18 PAGES)
            if (filteredEnterpriseFuture.isNotEmpty()) {
                item {
                    HorizontalDivider(color = Navy700, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🏛️ ENTERPRISE & FUTURE (18 PAGES)",
                            color = PurpleTech,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text("${filteredEnterpriseFuture.size}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                items(filteredEnterpriseFuture) { item ->
                    SidebarItemRow(item = item, isSelected = currentRoute == item.screen.route, onClick = { onNavigate(item.screen.route) })
                }
            }
        }

        // --- 4. User Session & Identity Bottom Bar ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { onOpenAuth() }
                .testTag("sidebar_user_session_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            border = BorderStroke(1.dp, if (authState.currentUser?.role == UserRole.ADMIN) AmberOrange.copy(alpha = 0.5f) else CyberCyan.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (authState.currentUser?.role == UserRole.ADMIN) AmberOrange.copy(alpha = 0.25f) else CyberCyan.copy(alpha = 0.25f))
                        .border(1.dp, if (authState.currentUser?.role == UserRole.ADMIN) AmberOrange else CyberCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = authState.currentUser?.fullName?.take(1) ?: "A",
                        color = if (authState.currentUser?.role == UserRole.ADMIN) AmberOrange else CyberCyan,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = authState.currentUser?.fullName ?: "Alex Morgan",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        if (authState.currentUser?.role == UserRole.ADMIN) {
                            Surface(color = AmberOrange.copy(alpha = 0.25f), shape = RoundedCornerShape(4.dp)) {
                                Text("ADMIN", color = AmberOrange, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                            }
                        }
                    }
                    Text(
                        text = authState.currentUser?.email ?: "alex.morgan@smartbank.ai",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }

                Surface(
                    color = EmeraldSuccess.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, EmeraldSuccess.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(EmeraldSuccess))
                        Text("ACTIVE", color = EmeraldSuccess, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SidebarItemRow(
    item: NavCategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.screen.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextWhite else TextWhite.copy(alpha = 0.85f)
                    )
                    Text(
                        text = item.subtitle,
                        fontSize = 9.sp,
                        color = if (isSelected) item.tagColor else TextMuted,
                        maxLines = 1
                    )
                }

                if (item.badgeLabel != null) {
                    Surface(
                        color = item.tagColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.5.dp, item.tagColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = item.badgeLabel,
                            color = item.tagColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        },
        icon = {
            Icon(
                imageVector = item.screen.icon,
                contentDescription = item.screen.title,
                tint = if (isSelected) item.tagColor else TextMuted,
                modifier = Modifier.size(18.dp)
            )
        },
        selected = isSelected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = item.tagColor.copy(alpha = 0.15f),
            selectedIconColor = item.tagColor,
            selectedTextColor = TextWhite,
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("sidebar_nav_${item.screen.route}")
    )
}
