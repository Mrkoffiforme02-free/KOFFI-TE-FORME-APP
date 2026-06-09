package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

// Project data model
data class Project(
    val id: Int,
    val title: String,
    val category: String, // Affiches, Flyers, Branding, Mock-ups
    val shortDesc: String,
    val fullDesc: String,
    val imageResName: String // Drawables fallback or local simulation
)

// Course data model
data class Course(
    val id: Int,
    val title: String,
    val price: Int,
    val oldPrice: Int,
    val urlChariow: String,
    val description: String,
    val imageResName: String
)

sealed interface PaymentState {
    object Idle : PaymentState
    object Processing : PaymentState
    data class Success(val transactionId: String, val paidAmount: Int) : PaymentState
    data class Error(val message: String) : PaymentState
}

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    // Bottom Navigation tab index: 0=Accueil, 1=Portfolio, 2=Formations, 3=À Propos, 4=Profil/Contact
    private val _currentTabIndex = MutableStateFlow(0)
    val currentTabIndex: StateFlow<Int> = _currentTabIndex.asStateFlow()

    // Portfolio category filter
    private val _selectedPortfolioCategory = MutableStateFlow("Tous")
    val selectedPortfolioCategory: StateFlow<String> = _selectedPortfolioCategory.asStateFlow()

    // Selected project for modal/dialog view details
    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject.asStateFlow()

    // Reactive streams from Room Database
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val boughtCourses: StateFlow<List<BoughtCourse>> = repository.boughtCourses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val contactMessages: StateFlow<List<ContactMessage>> = repository.contactMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quoteRequests: StateFlow<List<QuoteRequest>> = repository.quoteRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart summary
    val cartTotal: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartCount: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Forms states
    private val _contactName = MutableStateFlow("")
    val contactName = _contactName.asStateFlow()

    private val _contactEmail = MutableStateFlow("")
    val contactEmail = _contactEmail.asStateFlow()

    private val _contactSubject = MutableStateFlow("Graphisme")
    val contactSubject = _contactSubject.asStateFlow()

    private val _contactMessageText = MutableStateFlow("")
    val contactMessageText = _contactMessageText.asStateFlow()

    private val _isContactSubmitting = MutableStateFlow(false)
    val isContactSubmitting = _isContactSubmitting.asStateFlow()

    private val _contactFeedback = MutableStateFlow<String?>(null)
    val contactFeedback = _contactFeedback.asStateFlow()

    // Quote states
    private val _quoteProjectType = MutableStateFlow("Affiche")
    val quoteProjectType = _quoteProjectType.asStateFlow()

    private val _quoteBudget = MutableStateFlow(25000) // Default 25k XOF
    val quoteBudget = _quoteBudget.asStateFlow()

    private val _quoteDescription = MutableStateFlow("")
    val quoteDescription = _quoteDescription.asStateFlow()

    private val _isQuoteSubmitting = MutableStateFlow(false)
    val isQuoteSubmitting = _isQuoteSubmitting.asStateFlow()

    private val _quoteFeedback = MutableStateFlow<String?>(null)
    val quoteFeedback = _quoteFeedback.asStateFlow()

    // Moneroo Payment Screen Flow status
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState = _paymentState.asStateFlow()

    private val _paymentPhoneNumber = MutableStateFlow("")
    val paymentPhoneNumber = _paymentPhoneNumber.asStateFlow()

    private val _paymentNetwork = MutableStateFlow("MTN Mobile Money")
    val paymentNetwork = _paymentNetwork.asStateFlow()

    // Static project database (9 projects)
    val projects = listOf(
        Project(
            1, "Tech Conference Poster", "Affiches",
            "Affiche moderne et futuriste pour un sommet technologique.",
            "Une affiche conçue pour un sommet d'innovation technologique africaine, exploitant des lignes de code fluorescentes et des effets de perspective 3D sur fond sombre pour captiver un public de développeurs et d'entrepreneurs.",
            "proj_tech"
        ),
        Project(
            2, "Digital Agency Flyer", "Flyers",
            "Flyer promotionnel à fort impact pour une agence digitale.",
            "Un dépliant axé sur la conversion client pour une agence de marketing 360. Utilise des contrastes forts, des typographies dynamiques et des espacements équilibrés pour mettre en avant les services clés.",
            "proj_flyer_agency"
        ),
        Project(
            3, "Luxury Brand Identity", "Branding",
            "Charte graphique haut de gamme pour une marque de luxe.",
            "Conception complète de l'identité visuelle d'une marque de haute parfumerie. Logo raffiné, typographies sur mesure, palettes dorées et noires, transmettant l'exclusivité et la pureté.",
            "proj_luxury"
        ),
        Project(
            4, "Music Festival Poster", "Affiches",
            "Affiche dynamique électro pour festival de musique.",
            "Affiche événementielle pour un festival d'Afro-Electro. Couleurs néon vives fusionnées avec des textures tribales stylisées, provoquant une sensation de mouvement et de rythme dès le premier regard.",
            "proj_music"
        ),
        Project(
            5, "Consulting Services Flyer", "Flyers",
            "Prospectus élégant pour cabinet de conseil.",
            "Affiche promotionnelle pour un cabinet d'audit et conseil. Choix rigoureux de bleus profonds et de touches blanches, soulignant le professionnalisme, la rigueur et la confiance.",
            "proj_flyer_consult"
        ),
        Project(
            6, "Premium Product Packaging", "Mock-ups",
            "Maquette 3D de packaging de chocolat bio premium.",
            "Création d'un mock-up de packaging écologique pour une marque de chocolat bio africain haut de gamme. Intègre des motifs traditionnels dorés découpés subtilement.",
            "proj_pack"
        ),
        Project(
            7, "Social Media Kit", "Branding",
            "Gabarits de posts Instagram professionnels pour e-commerçants.",
            "Pack complet de visuels pour réseaux sociaux (carrousels, stories, posts de vente). Structuré pour une cohérence chromatique de marque, facilitant l'engagement de la communauté.",
            "proj_social"
        ),
        Project(
            8, "Fashion Brand Poster", "Affiches",
            "Campagne d'affichage Streetwear contemporain.",
            "Série d'affiches urbaines pour le lancement d'une collection de mode streetwear. Superposition de collages photo bruts et de graffitis vert néon saisissant l'essence de la culture urbaine.",
            "proj_fashion"
        ),
        Project(
            9, "Website Landing Page", "Mock-ups",
            "Maquette UX/UI d'une application de livraison rapide.",
            "Maquette interactive desktop et mobile d'une landing page produit. Travail poussé sur la clarté visuelle, l'arborescence de navigation et des appels à l'action proéminents du produit.",
            "proj_landing"
        )
    )

    // Static courses list (5 courses)
    val courses = listOf(
        Course(
            1,
            "ÉCONOMISE SANS TE PRIVER",
            4200,
            6500,
            "https://mrkoffiformm.mychariow.shop/economelibre",
            "Découvrez la méthode éprouvée pour gérer vos finances personnelles au quotidien sans vous priver de vos petits plaisirs.",
            "course_finance"
        ),
        Course(
            2,
            "APPRENDS À VENDRE ET À TE VENDRE AUTREMENT",
            4800,
            10000,
            "https://mrkoffiformm.mychariow.shop/marketeurpro",
            "Maîtrisez le personal branding et l'art de valoriser vos services de freelance pour attirer des clients prêts à payer le prix fort.",
            "course_marketing"
        ),
        Course(
            3,
            "DEVIENS LE G.O.A.T DANS L'ART DE CONVAINCRE",
            4800,
            10000,
            "https://mrkoffiformm.mychariow.shop/prdconvaincr",
            "L'art ultime de la négociation et de la persuasion psychologique appliqué à la vente de prestations de design ou d'e-commerce.",
            "course_sales"
        ),
        Course(
            4,
            "FORMATION COMPLÈTE GRAPHISME MOBILE PIXELLAB",
            4999,
            15000,
            "https://mrkoffiformm.mychariow.shop/learnpixellab",
            "La bible absolue pour devenir un as du design graphique en utilisant uniquement votre smartphone et l'application Pixellab.",
            "course_pixellab"
        ),
        Course(
            5,
            "LA PRISE DE PAROLE EN PUBLIQUE",
            3400,
            5200,
            "https://mrkoffiformm.mychariow.shop/parolegoat",
            "Vainquez le trac et captivez n'importe quelle audience grâce à des techniques simples d'expression corporelle et vocale.",
            "course_public_speaking"
        )
    )

    // Tab Navigation Actions
    fun navigateToTab(index: Int) {
        _currentTabIndex.value = index
    }

    // Portfolio filters
    fun setPortfolioCategory(category: String) {
        _selectedPortfolioCategory.value = category
    }

    fun setSelectedProject(project: Project?) {
        _selectedProject.value = project
    }

    // Shopping Cart actions
    fun addCourseToCart(course: Course) {
        viewModelScope.launch {
            // Check if already in cart
            val existing = cartItems.value.find { it.courseId == course.id }
            if (existing == null) {
                val cartItem = CartItem(
                    courseId = course.id,
                    title = course.title,
                    price = course.price,
                    oldPrice = course.oldPrice,
                    imageUrlString = course.imageResName
                )
                repository.insertCartItem(cartItem)
            }
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            repository.deleteCartItem(cartItem)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Contact form inputs updates
    fun setContactName(value: String) { _contactName.value = value }
    fun setContactEmail(value: String) { _contactEmail.value = value }
    fun setContactSubject(value: String) { _contactSubject.value = value }
    fun setContactMessageText(value: String) { _contactMessageText.value = value }

    fun submitContactForm() {
        val name = _contactName.value.trim()
        val email = _contactEmail.value.trim()
        val subject = _contactSubject.value
        val msg = _contactMessageText.value.trim()

        if (name.isEmpty() || email.isEmpty() || msg.isEmpty()) {
            _contactFeedback.value = "Veuillez remplir tous les champs obligatoires."
            return
        }

        viewModelScope.launch {
            _isContactSubmitting.value = true
            _contactFeedback.value = null
            
            // Insert into local Database history
            val contact = ContactMessage(
                name = name,
                email = email,
                subject = subject,
                message = msg,
                timestamp = System.currentTimeMillis()
            )
            repository.insertContactMessage(contact)
            
            kotlinx.coroutines.delay(1000) // Aesthetic simulated delay
            _isContactSubmitting.value = false
            _contactFeedback.value = "Votre message de contact a été envoyé avec succès !"
            
            // Reset form
            _contactName.value = ""
            _contactEmail.value = ""
            _contactMessageText.value = ""
        }
    }

    fun dismissContactFeedback() {
        _contactFeedback.value = null
    }

    // Quote form inputs updates
    fun setQuoteProjectType(value: String) { _quoteProjectType.value = value }
    fun setQuoteBudget(value: Int) { _quoteBudget.value = value }
    fun setQuoteDescription(value: String) { _quoteDescription.value = value }

    fun submitQuoteForm() {
        val pType = _quoteProjectType.value
        val budget = _quoteBudget.value
        val desc = _quoteDescription.value.trim()

        if (desc.isEmpty()) {
            _quoteFeedback.value = "Veuillez décrire brièvement votre projet."
            return
        }

        viewModelScope.launch {
            _isQuoteSubmitting.value = true
            _quoteFeedback.value = null
            
            // Insert into local Database history
            val quote = QuoteRequest(
                projectType = pType,
                budget = budget,
                description = desc,
                timestamp = System.currentTimeMillis()
            )
            repository.insertQuoteRequest(quote)
            
            kotlinx.coroutines.delay(1000) // Aesthetic delay
            _isQuoteSubmitting.value = false
            _quoteFeedback.value = "Votre demande de devis pour '$pType' a bien été transmise !"
            
            // Reset inputs
            _quoteDescription.value = ""
        }
    }

    fun dismissQuoteFeedback() {
        _quoteFeedback.value = null
    }

    // Payment controls
    fun updatePaymentPhoneNumber(value: String) {
        _paymentPhoneNumber.value = value
    }

    fun updatePaymentNetwork(value: String) {
        _paymentNetwork.value = value
    }

    fun startCheckout() {
        _paymentState.value = PaymentState.Idle
    }

    fun submitMonerooPayment() {
        val phone = _paymentPhoneNumber.value.trim()
        val total = cartTotal.value

        if (phone.isEmpty() || phone.length < 8) {
            _paymentState.value = PaymentState.Error("Numéro de téléphone invalide. Il doit faire au moins 8 chiffres.")
            return
        }

        viewModelScope.launch {
            _paymentState.value = PaymentState.Processing
            
            // Simulate Moneroo secure checkout handshaking
            kotlinx.coroutines.delay(2000)
            
            val txId = "MONX-" + UUID.randomUUID().toString().take(12).uppercase()
            
            // On payment success:
            // 1. Add all courses in cart to purchased db
            val currentCart = cartItems.value
            for (item in currentCart) {
                val bought = BoughtCourse(
                    courseId = item.courseId,
                    title = item.title,
                    purchasedAt = System.currentTimeMillis(),
                    transactionId = txId
                )
                repository.insertBoughtCourse(bought)
            }
            
            // 2. Clear shopping cart
            repository.clearCart()
            
            // 3. Set success state
            _paymentState.value = PaymentState.Success(txId, total)
            _paymentPhoneNumber.value = ""
        }
    }

    fun dismissPayment() {
        _paymentState.value = PaymentState.Idle
    }
}
