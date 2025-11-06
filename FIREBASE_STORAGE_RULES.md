# 🔥 Configurar Regras do Firebase Storage

## ⚠️ Problema Identificado

Os logs mostram que **o `coverUrl` está vazio** no Firestore, o que significa que o upload da imagem para o Firebase Storage pode estar **falhando silenciosamente**.

Você também está vendo este erro:
```
Error getting App Check token; using placeholder token instead.
```

## 📋 Passos para Configurar o Firebase Storage

### 1. Acesse o Console do Firebase
1. Vá para: https://console.firebase.google.com/
2. Selecione seu projeto **GamesApp**
3. No menu lateral, clique em **Storage**

### 2. Crie o Storage (se ainda não existe)
Se você não configurou o Storage ainda:
1. Clique em **Get Started**
2. Escolha o local (preferencialmente o mesmo do Firestore)
3. Clique em **Done**

### 3. Configure as Regras de Segurança

No console do Firebase Storage:
1. Clique na aba **Rules**
2. **Substitua** as regras existentes por estas:

```
rules_version = '2';

service firebase.storage {
  match /b/{bucket}/o {
    // Permitir que usuários autenticados leiam e escrevam apenas em suas próprias pastas
    match /users/{userId}/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

3. Clique em **Publish**

### 4. Teste Novamente

Agora que as regras estão configuradas:

1. **Recompile o app:**
   ```bash
   cd /Users/levilunique/Workspace/Kotlin/Android/PUCPR/aula_2
   ./gradlew assembleDebug
   ```

2. **Instale no dispositivo/emulador**

3. **Crie um novo jogo COM capa**

4. **Verifique os logs no Logcat** filtrando por "DEBUG" e "ERROR":

   **Logs esperados de SUCESSO:**
   ```
   DEBUG StorageRepository.uploadCover: Iniciando upload...
   DEBUG StorageRepository.uploadCover: uid = <seu-uid>
   DEBUG StorageRepository.uploadCover: docId = <doc-id>
   DEBUG StorageRepository.uploadCover: path = users/<uid>/covers/<doc-id>.jpg
   DEBUG StorageRepository.uploadCover: localUri = content://...
   DEBUG StorageRepository.uploadCover: Upload concluído, bytes transferidos = 123456
   DEBUG StorageRepository.uploadCover: Download URL obtida = https://firebasestorage...
   DEBUG CreateGameViewModel: cover uploadada = https://firebasestorage...
   DEBUG FirestoreGameRepository.upsert: coverUrl = 'https://firebasestorage...'
   ```

   **Se houver ERRO, você verá:**
   ```
   ERROR StorageRepository.uploadCover: Falha no upload! <TipoDoErro>: <mensagem>
   ```

5. **Edite o jogo** e veja se a capa aparece agora

## 🔍 Possíveis Erros e Soluções

### Erro: "User does not have permission to access"
- **Causa:** As regras do Storage não permitem o upload
- **Solução:** Verifique se as regras acima foram aplicadas corretamente
- **Verifique:** O usuário está autenticado? (check Firebase Auth no console)

### Erro: "Object does not exist"
- **Causa:** O caminho do arquivo está incorreto
- **Solução:** Verifique os logs do path: `users/<uid>/covers/<doc-id>.jpg`

### Erro: "Network error" ou "Timeout"
- **Causa:** Problemas de conectividade
- **Solução:** Verifique a conexão com a internet

### Erro: "Quota exceeded"
- **Causa:** Você excedeu o limite gratuito do Firebase
- **Solução:** Verifique o plano no console do Firebase

## 📱 Verificar no Console do Firebase

Após criar um jogo com capa:

1. Vá ao **Firebase Console** → **Storage**
2. Navegue para: `users/<seu-uid>/covers/`
3. Você deve ver os arquivos `.jpg` das capas
4. Clique em um arquivo para ver a URL de download

Se os arquivos aparecerem lá, mas o `coverUrl` ainda estiver vazio no Firestore:
- O problema está no segundo `upsert()` do `CreateGameViewModel`
- Verifique os logs para esse segundo `upsert`

## 🎯 Próximos Passos

1. ✅ Configure as regras do Storage (acima)
2. ✅ Recompile e instale o app
3. ✅ Crie um novo jogo com capa
4. ✅ Verifique os logs (especialmente ERROR)
5. ✅ Me envie os logs completos da criação do jogo
6. ✅ Verifique no console do Storage se o arquivo foi uploadado

## 💡 Dica

Se quiser testar com regras mais permissivas (apenas para DEBUG):
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

**⚠️ ATENÇÃO:** Essas regras permitem que qualquer usuário autenticado acesse qualquer arquivo. Use apenas para testes!

