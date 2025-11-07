# 🎮 GamesApp - Gerenciador de Coleção de Jogos

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-API%2024+-green.svg)](https://developer.android.com)
[![Firebase](https://img.shields.io/badge/Firebase-Latest-orange.svg)](https://firebase.google.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-blue.svg)](https://developer.android.com/jetpack/compose)

Aplicativo Android desenvolvido em Kotlin com Jetpack Compose para gerenciamento de coleção pessoal de jogos, utilizando Firebase como Backend-as-a-Service (MBaaS).

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Firebase Services](#firebase-services)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração do Firebase](#configuração-do-firebase)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Capturas de Tela](#capturas-de-tela)
- [Autor](#autor)

## 🎯 Sobre o Projeto

O **GamesApp** é um aplicativo móvel que permite aos usuários gerenciar sua coleção pessoal de jogos de forma simples e intuitiva. Com integração completa com Firebase, oferece autenticação segura, armazenamento em nuvem e sincronização automática entre dispositivos.

🎥 **Apresentação em vídeo:** [Assista no YouTube](https://youtu.be/s_c4h01kfqs)

### 🎓 Contexto Acadêmico

Projeto desenvolvido como Trabalho Final da disciplina de **Webservices e MBaaS** da **PUCPR**, demonstrando o uso prático de:
- Mobile Backend as a Service (MBaaS)
- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- Arquitetura MVVM
- Jetpack Compose

## ✨ Funcionalidades

### 🔐 Autenticação e Gerenciamento de Conta

- **Login/Registro de Usuários**
  - Autenticação via Firebase Authentication
  - Validação robusta de email e senha
  - Indicadores visuais de requisitos de senha
  - Recuperação de senha por email
  
- **Gerenciamento de Perfil**
  - Alteração de email com re-autenticação
  - Alteração de senha com validação de requisitos
  - Exclusão de conta (remove todos os dados)
  - Logout seguro

### 🎮 Gerenciamento de Jogos

- **CRUD Completo**
  - ✅ Criar novos jogos com informações detalhadas
  - 📖 Visualizar lista de jogos com filtros e ordenação
  - ✏️ Editar jogos existentes
  - 🗑️ Excluir jogos (individual ou em lote)

- **Informações do Jogo**
  - Título
  - Plataforma
  - Status (Backlog, Jogando, Concluído)
  - Avaliação (0-5 estrelas)
  - Capa personalizada (upload de imagens)
  - Notas pessoais

### 🖼️ Gerenciamento de Imagens

- Upload de capas de jogos
- Preview de imagens antes de salvar
- Exclusão de capas com ícone sobreposto
- Otimização automática de armazenamento
- Limpeza de imagens antigas ao atualizar

### 🔍 Funcionalidades Avançadas

- **Busca e Filtros**
  - Busca por nome do jogo
  - Filtro por status
  - Ordenação por nome, plataforma ou status
  
- **Seleção em Lote**
  - Seleção múltipla de jogos
  - Exclusão em massa
  - Contador de itens selecionados

- **Confirmações de Segurança**
  - Diálogos de confirmação para ações destrutivas
  - Re-autenticação para operações sensíveis
  - Validação de senha para exclusão de conta

## 🛠️ Tecnologias Utilizadas

### Frontend Mobile

- **[Kotlin](https://kotlinlang.org/)** - Linguagem principal
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** - UI Toolkit moderna
- **[Material Design 3](https://m3.material.io/)** - Design system
- **[Coil](https://coil-kt.github.io/coil/)** - Carregamento de imagens

### Backend (Firebase)

- **[Firebase Authentication](https://firebase.google.com/products/auth)** - Autenticação de usuários
- **[Cloud Firestore](https://firebase.google.com/products/firestore)** - Banco de dados NoSQL
- **[Firebase Storage](https://firebase.google.com/products/storage)** - Armazenamento de arquivos

### Arquitetura e Padrões

- **MVVM (Model-View-ViewModel)** - Arquitetura principal
- **Repository Pattern** - Abstração de acesso a dados
- **Kotlin Coroutines** - Programação assíncrona
- **StateFlow/State** - Gerenciamento de estado
- **Dependency Injection** - ViewModels

## 🏗️ Arquitetura

O projeto segue o padrão **MVVM** com separação clara de responsabilidades:

```
┌─────────────────┐
│      View       │  (Jetpack Compose Screens)
│   (UI Layer)    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   ViewModel     │  (Business Logic)
│ (Presentation)  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Repository    │  (Data Access Layer)
│  (Data Layer)   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Firebase SDK   │  (Remote Data Source)
│   (MBaaS)       │
└─────────────────┘
```

### Camadas

1. **UI Layer (View)**
   - Composables do Jetpack Compose
   - Telas de Login, Registro, Lista, Criação, Edição
   - Componentes reutilizáveis (Dialogs, Cards)

2. **Presentation Layer (ViewModel)**
   - Lógica de negócio
   - Gerenciamento de estado
   - Comunicação com Repository

3. **Data Layer (Repository)**
   - `FirestoreGameRepository` - Acesso ao Firestore
   - `StorageRepository` - Gerenciamento de Storage
   - Abstração de fontes de dados

4. **MBaaS Layer (Firebase)**
   - Firebase Authentication
   - Cloud Firestore
   - Firebase Storage

## 🔥 Firebase Services

### 1. Firebase Authentication

**Funcionalidades Implementadas:**
- Registro de novos usuários
- Login com email/senha
- Recuperação de senha
- Alteração de email
- Alteração de senha
- Re-autenticação para operações sensíveis
- Exclusão de conta

**Segurança:**
- Validação client-side antes de enviar para servidor
- Requisitos fortes de senha
- Re-autenticação obrigatória para operações críticas

### 2. Cloud Firestore

**Estrutura do Banco:**
```
users/
  {userId}/
    games/
      {gameId}/
        - title: String
        - platform: String
        - status: String
        - rating: Int
        - coverUrl: String
        - notes: String (opcional)
```

**Operações:**
- ✅ Create: Adicionar novos jogos
- ✅ Read: Listar jogos com ordenação
- ✅ Update: Atualizar informações
- ✅ Delete: Remover jogos

**Segurança:**
- Dados isolados por usuário (cada usuário só acessa seus próprios dados)
- Validação de autenticação em todas operações

### 3. Firebase Storage

**Estrutura de Armazenamento:**
```
users/
  {userId}/
    covers/
      {gameId}.jpg
```

**Funcionalidades:**
- Upload de imagens de capa
- Download de URLs públicas
- Exclusão de imagens antigas
- Limpeza automática ao excluir conta

## 📦 Pré-requisitos

- **Android Studio** Hedgehog (2023.1.1) ou superior
- **JDK** 17 ou superior
- **Android SDK** API 24+ (Android 7.0+)
- **Conta Firebase** configurada

## 🚀 Instalação

### 1. Clone o Repositório

```bash
git clone https://github.com/LeviLunique/gamesapp-collection-manager
cd gamesapp
```

### 2. Abra no Android Studio

1. Abra o Android Studio
2. Selecione "Open an Existing Project"
3. Navegue até a pasta do projeto
4. Aguarde o Gradle sync

### 3. Configure o Firebase

Veja a seção [Configuração do Firebase](#configuração-do-firebase) abaixo.

### 4. Execute o Projeto

1. Conecte um dispositivo Android ou inicie um emulador
2. Clique em "Run" (▶️) ou pressione `Shift + F10`

## 🔧 Configuração do Firebase

### Passo 1: Criar Projeto no Firebase

1. Acesse [Firebase Console](https://console.firebase.google.com/)
2. Clique em "Adicionar projeto"
3. Siga o assistente de criação

### Passo 2: Adicionar App Android

1. No Firebase Console, clique em "Adicionar app" > Android
2. Package name: `br.pucpr.appdev.gamesapp`
3. Baixe o arquivo `google-services.json`
4. Coloque em `app/google-services.json`

### Passo 3: Ativar Serviços

#### Authentication
1. No console, vá em Authentication > Sign-in method
2. Ative "Email/Password"

#### Firestore Database
1. Vá em Firestore Database > Create database
2. Escolha modo "production" ou "test"
3. Selecione região mais próxima

#### Storage
1. Vá em Storage > Get started
2. Use as regras de segurança padrão

### Passo 4: Regras de Segurança (Firestore)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Permite que o usuário gerencie seu próprio documento
    match /users/{userId} {
      allow read, write, delete: if request.auth != null && request.auth.uid == userId;
      
      // Permite que o usuário gerencie seus jogos
      match /games/{gameId} {
        allow read, write, delete: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
```

### Passo 5: Regras de Segurança (Storage)

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/covers/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## 📁 Estrutura do Projeto

```
app/src/main/java/br/pucpr/appdev/gamesapp/
├── base/
│   ├── Constants.kt              # Constantes centralizadas
│   └── Navigation.kt              # Sistema de navegação
│
├── components/
│   ├── ConfirmationDialog.kt     # Dialog reutilizável de confirmação
│   └── DeleteAccountDialog.kt    # Dialog de exclusão de conta
│
├── model/
│   ├── GameItem.kt               # Model de jogo
│   ├── GameStatus.kt             # Enum de status
│   ├── IGameRepository.kt        # Interface do repository
│   ├── FirestoreGameRepository.kt # Implementação Firestore
│   └── StorageRepository.kt      # Gerenciamento Storage
│
├── screens/
│   ├── auth/
│   │   ├── LoginScreen.kt        # Tela de login
│   │   ├── RegisterScreen.kt     # Tela de registro
│   │   ├── ForgotPasswordScreen.kt
│   │   ├── ChangeEmailScreen.kt
│   │   ├── ChangePasswordScreen.kt
│   │   └── EditProfileScreen.kt
│   │
│   ├── CreateGameScreen.kt       # Criar jogo
│   ├── CreateGameViewModel.kt
│   ├── EditGameScreen.kt         # Editar jogo
│   ├── EditGameViewModel.kt
│   ├── ListGamesScreen.kt        # Listar jogos
│   └── ListGamesViewModel.kt
│
├── ui/theme/
│   ├── Color.kt                  # Cores do tema
│   ├── Theme.kt                  # Tema Material 3
│   └── Type.kt                   # Tipografia
│
└── MainActivity.kt                # Activity principal

app/src/main/res/
├── values/
│   └── strings.xml               # Todas as strings traduzidas
└── ...
```

## 📸 Capturas de Tela

### Autenticação
- **Login**: Tela de entrada com validação
- **Registro**: Criação de conta com requisitos visuais
- **Recuperar Senha**: Envio de link por email

### Gerenciamento de Jogos
- **Lista**: Visualização em cards com filtros
- **Criar**: Formulário completo com upload de imagem
- **Editar**: Edição com preview e exclusão de capa
- **Detalhes**: Informações completas do jogo

### Perfil
- **Editar Perfil**: Alteração de email e senha
- **Exclusão de Conta**: Com confirmação e senha

## 🔑 Características Técnicas

### Segurança
- ✅ Re-autenticação para operações sensíveis
- ✅ Validação client-side e server-side
- ✅ Isolamento de dados por usuário
- ✅ Regras de segurança no Firebase
- ✅ Senhas com requisitos fortes

### Performance
- ✅ Kotlin Coroutines para operações assíncronas
- ✅ StateFlow para estado reativo
- ✅ Lazy loading de imagens com Coil
- ✅ Queries otimizadas no Firestore

### UX/UI
- ✅ Material Design 3
- ✅ Loading states em todas operações
- ✅ Feedback visual imediato
- ✅ Diálogos de confirmação
- ✅ Mensagens de erro amigáveis
- ✅ Animações suaves

### Código Limpo
- ✅ Zero valores hard-coded
- ✅ Constantes centralizadas
- ✅ Componentes reutilizáveis
- ✅ Separation of Concerns
- ✅ Repository Pattern
- ✅ MVVM Architecture

## 🎓 Aprendizados do Projeto

### Firebase (MBaaS)
- Integração completa com Authentication, Firestore e Storage
- Configuração de regras de segurança
- Gerenciamento de estado com dados em tempo real

### Android/Kotlin
- Jetpack Compose moderno
- MVVM com ViewModels
- Kotlin Coroutines
- Navigation Component

### Boas Práticas
- Clean Architecture
- Repository Pattern
- Dependency Injection
- Error Handling
- Code Organization

## 📝 Licença

Este projeto foi desenvolvido para fins acadêmicos na PUCPR.

## 👨‍💻 Autor

**Levi Lunique Izidio da Silva**
- GitHub: [@LeviLunique](https://github.com/LeviLunique/)
- Email: levi.lunique@gmail.com
- LinkedIn: [Levi Lunique](https://linkedin.com/in/levi-lunique)

---

## 📚 Referências

- [Firebase Documentation](https://firebase.google.com/docs)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Material Design 3](https://m3.material.io/)

---

**Desenvolvido com ❤️ e ☕ na PUCPR**

