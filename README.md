# euris-test

[![CI/CD Tests](https://github.com/mansville/euris-test/actions/workflows/ci-cd.yaml/badge.svg)](https://github.com/mansville/euris-test/actions/workflows/ci-cd.yaml)
[![Latest Release](https://img.shields.io/github/v/release/mansville/euris-test?color=blue&label=Latest%20Release)](https://github.com/mansville/euris-test/releases/latest)
![Java 21](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-brightgreen.svg)

---

## 📖 Descrizione

Questo progetto rappresenta una simulazione di backend per un piccolo **E-Commerce**.

L'applicazione è sviluppata con **Java 21** e **Spring Boot 4.1.2** e adotta un'architettura di tipo enterprise basata su layer **(Controller, Service, Repository)**, esponendo **API REST** documentate con **OpenAPI / Swagger**.

### Funzionalità

- **Persistenza & Migrazioni**: La gestione del database è automatizzata usando **Flyway**
- **Anagrafica Clienti**: Creazione, ricerca per Codice Fiscale / Email e ricerca paginata (con **validazione** sui dati inseriti)
- **Catalogo Prodotti**: Creazione e ricerca di prodotti con codice univoco, **audit** sull'ultima modifica e gestione dello stock basata sulla **concorrenza pessimistica**
- **Gestione Ordini e Dettagli**:
  - Creazione di ordini associati a un cliente con righe di dettaglio sul prodotto e la quantità richiesta e informazioni di **audit**
  - Gestione del ciclo di vita dell'ordine (cancellazione inclusa) usando la **concorrenza ottimistica** e una lista di stati dedicati (`ORDINATO`, `CONSEGNATO`)
  - Controllo di consistenza transazionale sullo stock disponibile

---

## ⚙️ Prerequisiti

- **JRE / JDK 21+** installato sulla macchina
- **Git** per clonare il repository (opzionale, solo in caso di non utilizzo del JAR precompilato nell'ultima versione rilasciata)
- **PostgreSQL 18** o **Docker** (opzionale, solo per l'esecuzione con il profilo Spring `prod`)

---

## 🚀 Come Avviare il JAR in Locale

È possibile scaricare direttamente l'eseguibile precompilato dell'ultima versione rilasciata:

1. Scaricare il file `.jar` dell'ultima versione rilasciata su GitHub: 👉 **[Latest Release](https://github.com/mansville/euris-test/releases/latest)**
2. Aprire un terminale nella directory contenente il file appena scaricato
3. Avvia l'applicazione con il comando:
   ```bash
   java -jar app-<version>.jar
   ```
   *(Sostituisci `app-<version>.jar` con il nome esatto del file scaricato, ad esempio `java -jar app-v1.0.0.jar`)*

Di default l'applicazione si avvierà con il profilo Spring `dev`, inizializzando un database in-memory **H2** e creando la struttura usando **Flyway**. L'applicazione risponderà all'indirizzo `http://localhost:8080`.

---

## 🧪 Come Eseguire i Test

Per avviare in locale l'intera suite di test automatizzati:

1. **Clonare il repository**:
   ```bash
   git clone https://github.com/mansville/euris-test.git
   cd euris-test
   ```
2. **Lanciare `clean test` usando il Maven Wrapper**:
   - **Linux / macOS**:
     ```bash
     ./mvnw clean test
     ```
   - **Windows**:
     ```powershell
     .\mvnw.cmd clean test
     ```

---

## 🗄️ Utilizzo con Database PostgreSQL di Produzione

Il progetto è già interamente predisposto per connettersi a un vero database di produzione con **PostgreSQL 18**, sfruttando l'apposito profilo Spring `prod` configurato in `src/main/resources/application-prod.yaml`.

> [!NOTE]
> La **pipeline CI/CD** di **GitHub Actions** (`ci-cd.yaml`) include già un service container attivo con PostgreSQL 18 durante l'esecuzione del workflow, garantendo che i test, le migrazioni e l'impacchettamento siano già verificati anche a fronte di un'istanza reale.

In futuro, perciò, è già possibile eseguire o distribuire l'applicazione nelle seguenti modalità:

### 1. Avvio con PostgreSQL in Locale

Avviare un'istanza di PostgreSQL (porta `5432`, database `euris`, username `test`, password `test`, interamente personalizzabile modificando il file `src/main/resources/application-prod.yaml`):
```bash
java -jar target/app.jar --spring.profiles.active=prod
```
Oppure sovrascrivere i parametri di connessione direttamente nel terminale usando le variabili d'ambiente:
```bash
SPRING_PROFILES_ACTIVE=prod \
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/euris \
SPRING_DATASOURCE_USER=test \
SPRING_DATASOURCE_PASSWORD=test \
java -jar target/app.jar
```

### 2. Avvio con Docker & Docker Compose

Creare gli opportuni file `Dockerfile` e `docker-compose.yaml`, sfruttando il profilo Spring `prod` per avviare l'intera infrastruttura (applicazione Spring + database PostgreSQL) con un singolo comando:
```bash
docker compose up -d
```

---

## 🌐 Endpoint del Browser (Profilo `dev`)

Quando l'applicazione è in esecuzione con il profilo predefinito `dev`, sono accessibili nel browser i seguenti endpoint di gestione, collaudo e documentazione:

| Strumento          | Endpoint                                                                   | Note                                                                                                                  |
|:-------------------|:---------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------|
| **Console H2**     | [`http://localhost:8080/h2`](http://localhost:8080/h2)                     | **Url**: `jdbc:h2:mem:euris;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`<br>**Username**: `test`<br>**Password**: `test` |
| **OpenAPI (JSON)** | [`http://localhost:8080/docs`](http://localhost:8080/docs)                 | Documentazione OpenAPI 3.0 in formato JSON                                                                            |
| **Swagger UI**     | [`http://localhost:8080/swagger.html`](http://localhost:8080/swagger.html) | Interfaccia interattiva per esplorare e testare le API REST                                                           |

> [!TIP]
> Avviando con il profilo Spring `prod`, sia la console H2 sia OpenAPI / Swagger UI sono intenzionalmente disabilitati per rispettare la sicurezza degli ambienti di produzione.