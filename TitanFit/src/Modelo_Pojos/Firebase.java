package Modelo_Pojos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class Firebase {
    private String type;
    private String project_id;
    private String private_key_id;
    private String private_key;
    private String client_email;
    private String client_id;
    private String auth_uri;
    private String token_uri;
    private String auth_provider_x509_cert_url;
    private String client_x509_cert_url;
    private String universe_domain;

    public Firebase() {
        this.type = "service_account";
        this.project_id = "titanfit-bd4c0";
        this.private_key_id = "1276fa506dab54386de1cc39ae91f70f705b0c89";

        // 🔥 Deja los saltos de línea normales, sin doble backslash
        this.private_key = "-----BEGIN PRIVATE KEY-----\n"
                + "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCglsPuSGmFOCa+\n"
                + "en9u+FCCmijgiWH4mZEAwqMwfGNNGv0K5h62WAel6MSc/L86Asgl/VGhTvz/kN05\n"
                + "2KCUyNo01WGyJDBr9ARhlIfMGx+NnnuLFopCdmhPhKi7T+wubfjAuOyq9kLqolsB\n"
                + "xEw7iO4vwd78a9o9VmHqVeW5RQ1MWnH7kVwmqNyVSpiTtvy4441NHq2aF40gAiOJ\n"
                + "EjkbuoXkXkaAJmJKZ9h0UTPNItq7YbWtRouohEg7ETz+vMqizjULDLWDJlgl7uZf\n"
                + "5g1Eg5BhcyCwGc8F0nT5kXEBItfd/g8JYTsuS2IrT2K892RJlSfnhoRmNPsTO0bz\n"
                + "ZlWmrDLRAgMBAAECggEAA23kxjhepH0j+qpUAVMlKPzQxaa5ZF0nJwyA/p6RYkqi\n"
                + "v7JVUpCDrUvQsweEr+DyVDlaHmc46N43ky4ok7TMUBeEbBDwxcViuJC666VioZwS\n"
                + "SFCVaEDuDlCnZ8SXpxArITZxu17h4+/OoLWH6TeN2pA5ykslUG0BT0dBThkG5Sv6\n"
                + "lvpA6FSgeD9KTAj09/Ab/QnI97jYILw+9bNaW0ByvDwPqbrHHhgIKJy/4XcnaQzr\n"
                + "EdL1HihyOfWy7PuYxlDzUojjL3UF6LrQRRiX24yRxuCSI1nmlBGv17ATeSauQyeZ\n"
                + "FLUbu1+Uin2jCl4MufDbg16CJacOigPn/114MlbsYQKBgQDanpky5vo6vr+wBIXm\n"
                + "I9MgzSdst/eZW0McLywtFKrY0N5mpgG25WKNjfTe95yB/7IVUkTg0WT1C5sm0bSS\n"
                + "ytqfToijpR7mBGJ73LOapGl87hWAW7BnsPaJioGk1AFiEzbKuSOtpqzzHUUKZhnG\n"
                + "STJMmFYEzgN/S3p/AQhjTnyNeQKBgQC8DA4KmAcFMCCH6rKwKjkjNmGHw7PfW64q\n"
                + "BlZZ14mBO5jFRnG1NFYBtFkhqv0tb0F8LxmkDRKiCsyJ11Qs4lY/CKorOwHMuQ8d\n"
                + "PojjGMCoojjDvU+IcitqZ6sRvJrvvXNakvj3z6IboRPZ8GmgkGwaSN/rCDIuHdP8\n"
                + "Tez7EXbyGQKBgF6qzNYg9PxZOzJiUwUcQEF09Px8EUikH0RmHPAMggrPL28ttZJY\n"
                + "dDut2/ptKKWWbqwS6uabZedCtlWx+0qRcugHHt4WE3IxNeBC6rctOMyzYDYr/hB4\n"
                + "rdlTU95nO44XmeNhFlUM1tmTtCDTo5ecfJ50XNNJznPh4FJ8xQATrZWpAoGAB5Vo\n"
                + "346AAfqN5U1WxXn+cegYiNQgpepZnMJCqz7hAcA4igQsQ8WHjmwKiCwQJ8vr1SlF\n"
                + "HHOWg8hxtUFk2KaMGeCslC++A9aWwHQsKxRteJ+KMIKq+XK5Q4JIyN9VVEPMqiNx\n"
                + "PPU3sWuOlGR4rgwFAA5eUWk0NN3pFazC2OE8bckCgYAFuMejC48Ju7w5MUEFa7ei\n"
                + "FPKyKMpx5x7oE5ZDfy/PgorhLNxwGpQ5XMnqgGGgPpDgBdz0llz74pYS9zGc9YEO\n"
                + "4gR7CkZIiJ0Yvs2+qWs6jJRTeATM6PXMVocDo+hFdEXxJxMu3LoiFC5ObzRJ7xPf\n"
                + "KwmHN8G9WswMy8TjG5NJuw==\n"
                + "-----END PRIVATE KEY-----\n";

        this.client_email = "firebase-adminsdk-fbsvc@titanfit-bd4c0.iam.gserviceaccount.com";
        this.client_id = "113439668329821576882";
        this.auth_uri = "https://accounts.google.com/o/oauth2/auth";
        this.token_uri = "https://oauth2.googleapis.com/token";
        this.auth_provider_x509_cert_url = "https://www.googleapis.com/oauth2/v1/certs";
        this.client_x509_cert_url = "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40titanfit-bd4c0.iam.gserviceaccount.com";
        this.universe_domain = "googleapis.com";
    }

    public static void inicializarFirebase() throws Exception {
        if (FirebaseApp.getApps().isEmpty()) {
            Firebase config = new Firebase();
            String json = config.toJson();

            try (InputStream serviceAccount = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
        }
    }

    private String toJson() {
        String escapedKey = private_key.replace("\n", "\\n").replace("\r", "");
        return "{"
                + "\"type\":\"" + type + "\","
                + "\"project_id\":\"" + project_id + "\","
                + "\"private_key_id\":\"" + private_key_id + "\","
                + "\"private_key\":\"" + escapedKey + "\","
                + "\"client_email\":\"" + client_email + "\","
                + "\"client_id\":\"" + client_id + "\","
                + "\"auth_uri\":\"" + auth_uri + "\","
                + "\"token_uri\":\"" + token_uri + "\","
                + "\"auth_provider_x509_cert_url\":\"" + auth_provider_x509_cert_url + "\","
                + "\"client_x509_cert_url\":\"" + client_x509_cert_url + "\","
                + "\"universe_domain\":\"" + universe_domain + "\""
                + "}";
    }
}
