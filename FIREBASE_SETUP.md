# Instruções para Resolver os Erros do Firebase

## Problema
O IDE (JetBrains/Android Studio) não está reconhecendo as dependências do Firebase, causando erros de "Unresolved reference" para `auth`, `firestore`, e `Firebase`.

## Solução

### Passo 1: Sincronizar o Projeto com Gradle
No Android Studio/IntelliJ IDEA:
1. Clique em **File** → **Sync Project with Gradle Files**
2. Ou clique no ícone de elefante (Gradle) na barra de ferramentas e selecione **Reload All Gradle Projects**

### Passo 2: Invalidar Caches (se necessário)
Se a sincronização não resolver:
1. Vá em **File** → **Invalidate Caches...**
2. Selecione **Invalidate and Restart**

### Passo 3: Verificar o arquivo google-services.json
O arquivo `google-services.json` foi movido para a localização correta:
- **Localização:** `/app/google-services.json` (raiz do módulo app)
- Certifique-se de que este arquivo contém as configurações corretas do seu projeto Firebase

### Passo 4: Verificar as Dependências
As seguintes dependências já estão configuradas no `app/build.gradle.kts`:
```kotlin
implementation(platform(libs.firebase.bom))
implementation(libs.firebase.auth.ktx)
implementation(libs.firebase.firestore.ktx)
implementation(libs.firebase.storage.ktx)
implementation(libs.kotlinx.coroutines.play.services)
```

### Passo 5: Verificar o Plugin do Google Services
O plugin do Google Services está configurado em `app/build.gradle.kts`:
```kotlin
plugins {
    // ...
    alias(libs.plugins.google.services)
}
```

## Código Correto
O arquivo `FirebaseGameRepository.kt` já está com o código correto:

```kotlin
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreGameRepository : IGameRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    // ...
}
```

## Observação sobre Versão do Java
Se você encontrar problemas relacionados à versão do Java durante a sincronização:
- O projeto está configurado para usar Java 17
- Certifique-se de que você tem o JDK 17 instalado
- No Android Studio, vá em **File** → **Project Structure** → **SDK Location** e verifique o JDK

## Compilação via Terminal (Alternativa)
Se precisar compilar via terminal e tiver problemas com a versão do Java:

```bash
# Use o script de ambiente criado
source local.env
./gradlew clean build
```

Ou defina o JAVA_HOME manualmente:
```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew clean build
```

