package spock.lair.editor

import androidx.compose.runtime.*
import spock.lair.fileio.Storage
import spock.lair.strings.id
import spock.lair.strings.type


object Editor {

    private var content: MutableState<String> = mutableStateOf("")
    private var selected: MutableState<String> = mutableStateOf("")

    var message: MutableState<String> = mutableStateOf("")
    val items = mutableStateListOf<String>()

    var darkTheme by mutableStateOf(false)


    fun item(): String {
        if (selected.value.isNotBlank()) {
            return selected.value
        } else {
            return content.value
        }
    }

    fun setValue(value: String) {
        content.value = value
    }

    fun log(msg: String) {
        message.value = msg
    }

    fun checkFile(msg: String) {
        message.value = "file: ${msg} exists?: ${java.io.File(msg).exists()}"
    }

    fun clear() {
        content.value = ""
        items.clear()
    }

    fun cli(item: String) {
        when (item.id()) {
            "menu" -> mainMenu()
            "pwd" -> pwd()
            else -> {
                log("Editor (menu) unknown:${item()}!")
            }
        }
    }

    fun mainMenu() {
        items.clear()
        items.add("![browser]")
        items.add("![proxy]")
        items.add("![storage]")
        items.add("![worker]")
        items.add("![page]")
        log("Loaded Menu")
    }

    fun pwd() {
        log(System.getProperty("user.dir"))
    }

    fun save() {
        content
    }

    fun load() {
        Storage.load(content.value).getAll().forEach {
            addEntry(it.value)
        }
    }

    fun addEntry(text: String) {
        try {
            items.add(text)
        } catch (e: Exception) {
            log("Error reading lines: ${e.message}")
        }
    }
}


/*
![site](contadeal "ContaDeal")

![page](home "Home")
site: ContaDeal
language: ro

![section.hero]("Automatizează-ți contabilitatea")
subtitle: "Toate finanțele tale pe aceeași pagină"
description: "ContaDeal conectează facturile emise cu tranzacțiile bancare în timp real. Scapi de introducerea manuală a datelor și ai totul pregătit pentru contabilitate, rapid și conform legislației."
cta: "Începe acum gratuit"

![section.grid]("Funcționalități cheie")
layout: grid

![griditem.feature]("Emite și transmite facturi electronice")
description: "Direct prin sistemul național e-Factura. Fii conform cu cerințele ANAF fără complicații."

![griditem.feature]("Open banking")
description: "Conecte-ți conturile bancare și importă automat tranzacțiile."

![griditem.feature]("Potrivire automată")
description: "Facturile sunt asociate automat cu plățile bancare aferente."

![griditem.feature]("Mod offline")
description: "Lucrează fără conexiune la internet, cu sincronizare automată când revii online."

![griditem.feature]("Sincronizare eficientă")
description: "Datele se actualizează în timp real între toate dispozitivele."

![section]("Funcționalități cheie")
feature1: "Emite și transmite facturi electronice direct prin sistemul național e-Factura. ContaDeal te ajută să fii conform cu cerințele ANAF, eliminând procesul complicat de încărcare manuală a facturilor în platforma guvernamentală."
feature2: "Open banking – Conectează-ți conturile bancare și importă automat tranzacțiile. Situația financiară este actualizată în permanență, fără fișiere CSV sau introducerea manuală a extraselor bancare."
feature3: "Potrivire automată – Facturile emise sunt potrivite automat cu plățile aferente din contul tău bancar. Vezi imediat ce facturi sunt achitate și care sunt în așteptare, fără erori."
feature4: "Mod offline – Aplicația funcționează și fără conexiune la internet. Emite facturi sau înregistrează plăți oriunde, iar datele se sincronizează automat când revii online."
feature5: "Sincronizare eficientă – Toate datele financiare se sincronizează în timp real între dispozitive, fie că folosești telefonul sau calculatorul."

![section]("Cum funcționează")
step1: "Creează un cont – Înregistrează-te gratuit pe platforma ContaDeal. Introdu datele companiei tale și pregătește-te să automatizezi procesul financiar."
step2: "Conectează-ți sursele – Activează integrările esențiale. Conectează un cont bancar prin open banking securizat și, dacă ai nevoie, configurează integrarea cu sistemul eFactura."
step3: "Importă sau emite documente – Încarcă facturile existente (PDF) sau emite facturi noi direct în ContaDeal."
step4: "Automatizare în acțiune – ContaDeal potrivește automat fiecare factură emisă cu tranzacția bancară corespunzătoare. Toate informațiile sunt organizate și gata de verificare."
step5: "Rezultate și utilizare – Vizualizezi în dashboard situația încasărilor și plăților. Când ești gata, exporți datele pentru contabil sau arhivă, știind că totul este corect și la zi."

![section]("Planuri și prețuri")
plan_Gratuit: "Ideal pentru început – Gestionare facturi și plăți de bază, sincronizare cloud și backup zilnic, acces de pe un dispozitiv (web sau mobil). [Înscrie-te gratuit]"
plan_Banking: "Automatizare bancară completă – Tot ce oferă planul Gratuit, integrare open banking (1 cont bancar), potrivire automată facturi-plăți, suport standard prin email. [Alege planul Banking]"
plan_Business: "Pentru companii în creștere – Tot ce oferă planul Banking, integrare e-Factura (emitere și transmitere e-facturi), conturi bancare multiple, utilizatori multipli și suport prioritar dedicat. [Alege planul Business]"

![section]("De ce ContaDeal?")
point1: "Încredere și securitate – Datele sunt protejate prin criptare și stocate în siguranță, cu backup automat."
point2: "Conformitate legală – Aliniat la legislația fiscală actuală prin integrarea cu e-Factura și respectarea standardelor (ANAF, PSD2)."
point3: "Eficiență și performanță – Aplicația optimizează procesele, reducând timpul petrecut pe verificări manuale."

![section]("Întrebări frecvente")
q1: "ContaDeal înlocuiește un program de contabilitate sau un contabil?"
a1: "Automatizează colectarea și organizarea datelor, dar nu înlocuiește un expert contabil sau un software complet."
q2: "Cât de sigură este conexiunea cu banca?"
a2: "Integrarea este foarte sigură, conform PSD2. Se folosesc conexiuni criptate, iar accesul este read-only."
q3: "Pot emite facturi electronice (e-Factura) prin ContaDeal?"
a3: "Da, în planul Business poți emite și transmite e-facturi prin sistemul RO e-Factura al ANAF."
q4: "Funcționează aplicația și fără internet?"
a4: "Da, modul offline permite emiterea facturilor și înregistrarea plăților oriunde, sincronizând datele când revii online."
q5: "Am nevoie de experiență contabilă?"
a5: "Nu, platforma este concepută pentru a fi intuitivă și ușor de utilizat."
q6: "Cum asigură ContaDeal conformitatea financiară?"
a6: "Respectă ghidurile locale și standardele de securitate (inclusiv PSD2), evitând astfel eventualele amenzi."
q7: "Ce fac dacă întâmpin probleme?"
a7: "Suport dedicat este oferit, cu prioritate pentru planurile premium. Contactează echipa prin aplicație sau email."
q8: "Ce se întâmplă cu datele mele dacă nu mai folosesc ContaDeal?"
a8: "Datele pot fi exportate oricând, permițându-ți să păstrezi controlul complet asupra informațiilor."

![section]("SEO")
cta: "Ești pregătit să îți automatizezi finanțele? Încearcă ContaDeal și descoperă cât de simplă poate fi gestionarea contabilității. Începe acum gratuit"
keywords: "automatizare contabilitate, soft contabilitate online, program facturare online, e-Factura ANAF, facturi electronice, integrare e-Factura, open banking România, conectare cont bancar contabilitate, potrivire automată facturi plăți, gestionare ușoară a facturilor, eliminare introducere manuală date, conformitate ANAF PSD2, soluție facturare digitală, eficientizare contabilitate firmă, contabilitate IMM, soft pentru PFA și SRL, soluții financiare antreprenori, facturare și plăți automate, aplicație gestiune financiară

![site](prisma "PRISMA-Software")



![page.default](home "Home")
site: ContaDeal
language: ro

![section.hero](onboarding-welcome "Welcome to ContaDeal")
subtitle: Let's get you started
background: lightblue
<dark>
background: "#002244"
text_color: "#FFFFFF"

![section](dashboard-overview "Dashboard Overview")
widgets: 5
last_updated: 2 mins ago
<fallback>
message: No data available.

![section.steps](user-guide "User Guide")
steps: 3
audience: new users
1. Sign up with your email
2. Customize your profile
3. Explore features

![card.user](john_doe "John Doe")
title: Developer
subtitle: 30 years old

![task.dev](monitor "Monitor Worker Bundle")
targetDir: ../live
path: ../build/webpackbrowser/TODO/worker
file: worker.js


![section.hero](onboarding-welcome "Welcome to Platform")
subtitle: Let's get you started
background: lightblue
<dark>
background: "#002244"
text_color: "#FFFFFF"

![section](dashboard-overview "Dashboard Overview")
widgets: 5
last_updated: 2 mins ago
<fallback>
message: No data available.

![section.hero](feature-highlights "Feature Highlights")
feature_count: 3
image: features.png
# Top Improvements
- New AI assistant integration
- Faster load times
- Improved security protocols

![section](security-policies "Security Policies")
policies: 5
last_updated: 2025-03-01
contact: security@example.com
status: active
<error>
status: inaccessible
message: Access denied

![section.steps](user-guide "User Guide")
steps: 3
audience: new users
1. Sign up with your email
2. Customize your profile
3. Explore features

![section](faq "Frequently Asked Questions")
questions: 10

![section](analytics-summary "Analytics Summary")
users: 1200
conversion_rate: 4.5%
peak_time: 13:00
new_users: 300
background: "#ffffff"
text_color: "#000000"
<dark>
background: "#1e1e1e"
text_color: "#f0f0f0"



in a nutshell theres

![component.variant](id "some label")
some_prop: Some Value
 */