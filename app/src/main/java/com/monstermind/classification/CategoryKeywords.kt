package com.monstermind.classification

import com.monstermind.data.entity.MemoryCategory

/**
 * Keyword mappings for memory category classification.
 *
 * Used by CategoryClassifier to identify memory type based on OCR text.
 * Each category has a list of keywords that strongly indicate that category.
 *
 * Strategy:
 * - Keywords are matched case-insensitively
 * - Any keyword match contributes to that category's score
 * - Category with highest score is selected
 * - If no matches or ties, category is "Other"
 *
 * Easy to extend: Add new keywords to improve classification accuracy.
 * Can be replaced with ML model later (Green Light).
 *
 * Example:
 * Text: "Flight LH456 to Berlin, boarding at gate 12"
 * Keywords matched: "flight", "boarding"
 * Result: Travel category
 */
object CategoryKeywords {

    /**
     * Shopping category keywords.
     * Indicators: prices, products, discount, order, delivery, etc.
     */
    val shopping = setOf(
        // Price indicators
        "₹", "$", "€", "£", "price", "cost", "amount", "rupees", "dollars",

        // Purchase indicators
        "buy", "purchase", "order", "add to cart", "checkout", "cart",
        "payment", "card", "upi", "credit", "debit",

        // Product indicators
        "product", "item", "sku", "brand", "model", "variant",
        "size", "color", "quantity", "stock",

        // Commerce platforms
        "amazon", "flipkart", "ebay", "aliexpress", "shop",
        "store", "mall", "retailer", "vendor",

        // Discount/offer
        "discount", "offer", "deal", "sale", "coupon", "code",
        "promo", "cashback", "free", "reduced", "percent", "off",

        // Delivery
        "delivery", "shipping", "shipped", "tracking", "address",
        "home", "door step", "express",

        // Review
        "rating", "review", "stars", "quality", "value"
    )

    /**
     * Travel category keywords.
     * Indicators: flights, hotels, destinations, bookings, etc.
     */
    val travel = setOf(
        // Transportation
        "flight", "airline", "airport", "boarding", "gate",
        "ticket", "booking", "reservation", "plane", "air",
        "train", "railway", "station", "bus", "coach",
        "taxi", "cab", "car", "rental", "vehicle",
        "hotel", "resort", "motel", "accommodation", "stay",

        // Destinations
        "city", "country", "destination", "location", "tour",
        "travel", "trip", "vacation", "holiday", "journey",
        "visit", "explore", "tourist", "attraction",

        // Geographic
        "road", "street", "avenue", "place", "address",
        "map", "route", "route", "distance", "kilometer",
        "delhi", "mumbai", "london", "paris", "newyork",
        "bengaluru", "bangalore", "goa", "manali", "jaipur",

        // Booking specifics
        "check-in", "checkout", "duration", "night", "days",
        "dates", "from", "to", "departure", "arrival",
        "passport", "visa", "baggage", "luggage", "customs"
    )

    /**
     * Work/Professional category keywords.
     * Indicators: meetings, documents, projects, deadlines, etc.
     */
    val work = setOf(
        // Meetings/Communication
        "meeting", "call", "conference", "presentation", "webinar",
        "zoom", "teams", "slack", "email", "message",
        "agenda", "minutes", "notes", "discussion",

        // Work items
        "project", "task", "deadline", "milestone", "deliverable",
        "sprint", "backlog", "issue", "bug", "feature",
        "report", "analysis", "plan", "strategy", "goal",

        // Business
        "client", "company", "team", "manager", "colleague",
        "employee", "staff", "hr", "payroll", "benefits",
        "revenue", "sales", "profit", "margin", "budget",

        // Documents
        "document", "spreadsheet", "sheet", "excel", "pdf",
        "presentation", "deck", "slide", "proposal", "contract",
        "agreement", "document", "template",

        // Time
        "monday", "tuesday", "wednesday", "thursday", "friday",
        "week", "month", "quarter", "fy", "january", "february"
    )

    /**
     * Education category keywords.
     * Indicators: lectures, exams, textbooks, subjects, etc.
     */
    val education = setOf(
        // Academic levels
        "exam", "test", "quiz", "assignment", "homework",
        "class", "lecture", "lesson", "course", "subject",
        "school", "college", "university", "institute", "academy",

        // Subjects
        "dsa", "data structures", "algorithm", "programming",
        "java", "python", "kotlin", "code", "coding",
        "math", "mathematics", "algebra", "calculus", "geometry",
        "physics", "chemistry", "biology", "history", "literature",
        "english", "hindi", "language",

        // Educational activities
        "study", "learning", "note", "notes", "textbook",
        "chapter", "topic", "concept", "theory", "practice",
        "problem", "solution", "example", "tutorial",

        // Marks/Performance
        "grade", "gpa", "marks", "score", "result",
        "pass", "fail", "credit", "semester", "term",

        // Time
        "semester", "term", "school year", "academic"
    )

    /**
     * Gaming category keywords.
     * Indicators: game names, achievements, scores, etc.
     */
    val gaming = setOf(
        // Game titles/platforms
        "valorant", "pubg", "minecraft", "fortnite", "csgo",
        "dota", "league of legends", "call of duty", "overwatch",
        "gta", "fallout", "skyrim", "zelda", "mario",
        "among us", "roblox", "steam", "epic games",

        // Gaming actions
        "kill", "ace", "headshot", "victory", "win", "lose",
        "score", "points", "level", "achievement", "unlock",
        "quest", "mission", "challenge", "boss", "enemy",
        "loot", "item", "weapon", "armor", "skill",

        // Gaming terms
        "game", "play", "player", "team", "match",
        "tournament", "esports", "rank", "elo", "rating",
        "fps", "rpg", "mmo", "multiplayer", "single player",

        // In-game locations/terms
        "map", "spawn", "arena", "lobby", "chat"
    )

    /**
     * Events category keywords.
     * Indicators: concerts, festivals, conferences, parties, etc.
     */
    val events = setOf(
        // Event types
        "concert", "festival", "event", "show", "performance",
        "conference", "summit", "meeting", "expo", "fair",
        "party", "wedding", "ceremony", "celebration",
        "tournament", "competition", "race", "match",

        // Venue
        "arena", "auditorium", "stadium", "venue", "hall",
        "theater", "cinema", "club", "bar", "restaurant",
        "garden", "park", "ground", "field",

        // Ticket/Booking
        "ticket", "booking", "entry", "pass", "admission",
        "reserve", "book", "register", "enroll",

        // Artists/Performers
        "artist", "band", "musician", "singer", "performer",
        "speaker", "conductor", "orchestra",

        // Time
        "date", "time", "schedule", "october", "november",
        "december", "january", "2026", "2025",
        "today", "tomorrow", "this week", "next month"
    )

    /**
     * Notes category keywords.
     * Indicators: personal notes, ideas, lists, reminders, etc.
     */
    val notes = setOf(
        // Note-taking
        "note", "notes", "memo", "reminder", "reminder",
        "todo", "to-do", "checklist", "list", "idea",
        "thought", "brainstorm", "outline", "summary",

        // Formats
        "bullet", "point", "item", "topic", "section",
        "heading", "subheading", "paragraph",

        // Actions
        "remember", "don't forget", "important", "urgent",
        "note to self", "reminder", "follow up", "pending",
        "due", "action item"
    )

    /**
     * Get keywords for a specific category.
     */
    fun getKeywordsForCategory(category: String): Set<String> = when (category) {
        MemoryCategory.SHOPPING -> shopping
        MemoryCategory.TRAVEL -> travel
        MemoryCategory.WORK -> work
        MemoryCategory.EDUCATION -> education
        MemoryCategory.GAMING -> gaming
        MemoryCategory.EVENTS -> events
        MemoryCategory.NOTES -> notes
        else -> emptySet()
    }

    /**
     * Get all keywords across all categories (for stop word filtering if needed).
     */
    fun getAllKeywords(): Set<String> = setOf(
        *shopping.toTypedArray(),
        *travel.toTypedArray(),
        *work.toTypedArray(),
        *education.toTypedArray(),
        *gaming.toTypedArray(),
        *events.toTypedArray(),
        *notes.toTypedArray()
    )
}