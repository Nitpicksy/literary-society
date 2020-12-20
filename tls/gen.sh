KEY_SIZE=${1:-4096}

ZUUL_NAME=${2:-zuul}
ZUUL_ALIAS=${3:-zuul}
ZUUL_KEYSTORE_PASSWORD=${4:-password}

EUREKA_NAME=${5:-eureka}
EUREKA_ALIAS=${6:-eureka}
EUREKA_KEYSTORE_PASSWORD=${7:-password}

CLOUD_NAME=${8:-cloud}
CLOUD_ALIAS=${9:-cloud}
CLOUD_KEYSTORE_PASSWORD=${10:-password}

CLIENT_NAME=${11:-client}
CLIENT_ALIAS=${12:-client}
CLIENT_KEYSTORE_PASSWORD=${13:-password}

LITERARY_NAME=${14:-literary}
LITERARY_ALIAS=${15:-literary}
LITERARY_KEYSTORE_PASSWORD=${16:-password}

GATEWAY_NAME=${17:-gateway}
GATEWAY_ALIAS=${18:-gateway}
GATEWAY_KEYSTORE_PASSWORD=${19:-password}

PAYPAL_NAME=${20:-paypal}
PAYPAL_ALIAS=${21:-paypal}
PAYPAL_KEYSTORE_PASSWORD=${22:-password}

BITCOIN_NAME=${23:-bitcoin}
BITCOIN_ALIAS=${24:-bitcoin}
BITCOIN_KEYSTORE_PASSWORD=${25:-password}

BANK_NAME=${26:-bank}
BANK_ALIAS=${27:-bank}
BANK_KEYSTORE_PASSWORD=${28:-password}

PCC_NAME=${29:-pcc}
PCC_ALIAS=${30:-pcc}
PCC_KEYSTORE_PASSWORD=${31:-password}

# root ca
mkdir -p ca/root-ca/private ca/root-ca/db crl certs
chmod 700 ca/root-ca/private

# create database for root ca
cp /dev/null ca/root-ca/db/root-ca.db
cp /dev/null ca/root-ca/db/root-ca.db.attr
echo 01 > ca/root-ca/db/root-ca.crt.srl
echo 01 > ca/root-ca/db/root-ca.crl.srl


# create root ca request
openssl req \
    -new \
    -nodes \
    -config config/root-ca.conf \
    -out ca/root-ca.csr \
    -keyout ca/root-ca/private/root-ca.key || exit

# create root ca certificate
openssl ca \
    -selfsign \
    -rand_serial \
    -batch \
    -config config/root-ca.conf \
    -in ca/root-ca.csr \
    -out ca/root-ca.crt \
    -extensions root_ca_ext || exit

# create initial crl
openssl ca \
    -gencrl \
    -config config/root-ca.conf \
    -out crl/root-ca.crl || exit

# tls ca
mkdir -p ca/tls-ca/private ca/tls-ca/db crl certs
chmod 700 ca/tls-ca/private

# create database for tls ca
cp /dev/null ca/tls-ca/db/tls-ca.db
cp /dev/null ca/tls-ca/db/tls-ca.db.attr
echo 01 > ca/tls-ca/db/tls-ca.crt.srl
echo 01 > ca/tls-ca/db/tls-ca.crl.srl

# create tls ca request
openssl req \
    -new \
    -nodes \
    -config config/tls-ca.conf \
    -out ca/tls-ca.csr \
    -keyout ca/tls-ca/private/tls-ca.key || exit

# create tls ca certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/root-ca.conf \
    -in ca/tls-ca.csr \
    -out ca/tls-ca.crt \
    -extensions signing_ca_ext || exit

# create initial crl
openssl ca \
    -gencrl \
    -config config/tls-ca.conf \
    -out crl/tls-ca.crl || exit

cat ca/tls-ca.crt ca/root-ca.crt > \
    ca/tls-ca-chain.pem

mkdir -p "certs/${ZUUL_NAME}/keystore"

# Create Zuul request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${ZUUL_NAME}/${ZUUL_NAME}.csr" \
    -keyout "certs/${ZUUL_NAME}/${ZUUL_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=zuul" || exit

# Create Zuul certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${ZUUL_NAME}/${ZUUL_NAME}.csr" \
    -out "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -extensions server_ext || exit

# Export Zuul certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${ZUUL_ALIAS}" \
    -inkey "certs/${ZUUL_NAME}/${ZUUL_NAME}.key" \
    -in "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -passout "pass:${ZUUL_KEYSTORE_PASSWORD}" \
    -out "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.keystore.p12" || exit


mkdir -p "certs/${CLIENT_NAME}"

# Create TLS client request (for web browser)
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/client.conf \
    -out "certs/${CLIENT_NAME}/${CLIENT_NAME}.csr" \
    -keyout "certs/${CLIENT_NAME}/${CLIENT_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy Client AS/OU=Nitpicksy Certificate Authority/CN=Nitpicksy Client" || exit

# Create TLS client certificate (for web browser)
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${CLIENT_NAME}/${CLIENT_NAME}.csr" \
    -out "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -policy extern_pol \
    -extensions client_ext || exit

# Export TLS client certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${CLIENT_ALIAS}" \
    -inkey "certs/${CLIENT_NAME}/${CLIENT_NAME}.key" \
    -in "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -certfile ca/tls-ca-chain.pem \
    -passout "pass:${CLIENT_KEYSTORE_PASSWORD}" \
    -out "certs/${CLIENT_NAME}/${CLIENT_NAME}.p12" || exit


mkdir -p "certs/${EUREKA_NAME}/keystore"

# Create Eureka request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1,DNS:eureka-peer1,IP.2:127.0.0.1,DNS:eureka-peer2,IP.3:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${EUREKA_NAME}/${EUREKA_NAME}.csr" \
    -keyout "certs/${EUREKA_NAME}/${EUREKA_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=Eureka" || exit

# Create Eureka certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${EUREKA_NAME}/${EUREKA_NAME}.csr" \
    -out "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -extensions server_ext || exit

# Export Eureka certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${EUREKA_ALIAS}" \
    -inkey "certs/${EUREKA_NAME}/${EUREKA_NAME}.key" \
    -in "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -passout "pass:${EUREKA_KEYSTORE_PASSWORD}" \
    -out "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.keystore.p12" || exit

mkdir -p "certs/${CLOUD_NAME}/keystore"

# Create Cloud request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${CLOUD_NAME}/${CLOUD_NAME}.csr" \
    -keyout "certs/${CLOUD_NAME}/${CLOUD_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=Cloud" || exit

# Create Cloud certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${CLOUD_NAME}/${CLOUD_NAME}.csr" \
    -out "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -extensions server_ext || exit

# Export CLoud certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${CLOUD_ALIAS}" \
    -inkey "certs/${CLOUD_NAME}/${CLOUD_NAME}.key" \
    -in "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -passout "pass:${CLOUD_KEYSTORE_PASSWORD}" \
    -out "certs/${CLOUD_NAME}/keystore/${CLOUD_NAME}.keystore.p12" || exit


mkdir -p "certs/${LITERARY_NAME}/keystore"

# Create Literary society request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${LITERARY_NAME}/${LITERARY_NAME}.csr" \
    -keyout "certs/${LITERARY_NAME}/${LITERARY_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=Literary Society" || exit

# Create Literary certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${LITERARY_NAME}/${LITERARY_NAME}.csr" \
    -out "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -extensions server_ext || exit

# Export Literary certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${LITERARY_ALIAS}" \
    -inkey "certs/${LITERARY_NAME}/${LITERARY_NAME}.key" \
    -in "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -passout "pass:${LITERARY_KEYSTORE_PASSWORD}" \
    -out "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.keystore.p12" || exit

mkdir -p "certs/${GATEWAY_NAME}/keystore"

# Create Payment gateway request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.csr" \
    -keyout "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=Payment Gateway" || exit

# Create Payment gateway certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.csr" \
    -out "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -extensions server_ext || exit

# Export Gateway certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${GATEWAY_ALIAS}" \
    -inkey "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.key" \
    -in "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -passout "pass:${GATEWAY_KEYSTORE_PASSWORD}" \
    -out "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.keystore.p12" || exit

mkdir -p "certs/${PAYPAL_NAME}/keystore"

# Create Paypal request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.csr" \
    -keyout "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=Paypal" || exit

# Create Paypal certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.csr" \
    -out "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.crt" \
    -extensions server_ext || exit

# Export Paypal certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${PAYPAL_ALIAS}" \
    -inkey "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.key" \
    -in "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.crt" \
    -passout "pass:${PAYPAL_KEYSTORE_PASSWORD}" \
    -out "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.keystore.p12" || exit


mkdir -p "certs/${BITCOIN_NAME}/keystore"

# Create Bitcoin request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.csr" \
    -keyout "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=Bitcoin" || exit

# Create Bitcoin certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.csr" \
    -out "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.crt" \
    -extensions server_ext || exit

# Export Bitcoin certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${BITCOIN_ALIAS}" \
    -inkey "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.key" \
    -in "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.crt" \
    -passout "pass:${BITCOIN_KEYSTORE_PASSWORD}" \
    -out "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.keystore.p12" || exit

mkdir -p "certs/${BANK_NAME}/keystore"

# Create Bank request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${BANK_NAME}/${BANK_NAME}.csr" \
    -keyout "certs/${BANK_NAME}/${BANK_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=Bank" || exit

# Create Bank certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${BANK_NAME}/${BANK_NAME}.csr" \
    -out "certs/${BANK_NAME}/${BANK_NAME}.crt" \
    -extensions server_ext || exit

# Export Bank certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${BANK_ALIAS}" \
    -inkey "certs/${BANK_NAME}/${BANK_NAME}.key" \
    -in "certs/${BANK_NAME}/${BANK_NAME}.crt" \
    -passout "pass:${BANK_KEYSTORE_PASSWORD}" \
    -out "certs/${BANK_NAME}/keystore/${BANK_NAME}.keystore.p12" || exit

mkdir -p "certs/${PCC_NAME}/keystore"

# Create PCC request
SAN=DNS:nitpicksy.no,DNS:www.nitpicksy.no,DNS:localhost,IP.1:127.0.0.1 \
openssl req \
    -new \
    -nodes \
    -newkey "rsa:${KEY_SIZE}" \
    -config config/services.conf \
    -out "certs/${PCC_NAME}/${PCC_NAME}.csr" \
    -keyout "certs/${PCC_NAME}/${PCC_NAME}.key" \
    -subj "/C=NO/O=Nitpicksy/OU=Nitpicksy Certificate Authority/CN=PCC" || exit

# Create PCC certificate
openssl ca \
    -rand_serial \
    -batch \
    -config config/tls-ca.conf \
    -in "certs/${PCC_NAME}/${PCC_NAME}.csr" \
    -out "certs/${PCC_NAME}/${PCC_NAME}.crt" \
    -extensions server_ext || exit

# Export PCC certificate chain to keystore
openssl pkcs12 \
    -export \
    -name "${PCC_ALIAS}" \
    -inkey "certs/${PCC_NAME}/${PCC_NAME}.key" \
    -in "certs/${PCC_NAME}/${PCC_NAME}.crt" \
    -passout "pass:${PCC_KEYSTORE_PASSWORD}" \
    -out "certs/${PCC_NAME}/keystore/${PCC_NAME}.keystore.p12" || exit

# --- Generate truststores

#Into EUREKA
#imported: Root,TLS,zuul,cloud,client,literary,gateway,paypal,bitcoin,bank,pcc
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" && \
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${LITERARY_ALIAS}" \
    -file "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${GATEWAY_ALIAS}" \
    -file "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PAYPAL_ALIAS}" \
    -file "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BITCOIN_ALIAS}" \
    -file "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BANK_ALIAS}" \
    -file "certs/${BANK_NAME}/${BANK_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PCC_ALIAS}" \
    -file "certs/${PCC_NAME}/${PCC_NAME}.crt" \
    -storepass "${EUREKA_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${EUREKA_NAME}/keystore/${EUREKA_NAME}.truststore.p12" || exit

echo "truststore eureka - done"

#Into ZUUL
# Import Root,TLS,eureka,cloud,client,literary,gateway,paypal,bitcoin,bank,pcc
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${LITERARY_ALIAS}" \
    -file "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${GATEWAY_ALIAS}" \
    -file "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PAYPAL_ALIAS}" \
    -file "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BITCOIN_ALIAS}" \
    -file "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BANK_ALIAS}" \
    -file "certs/${BANK_NAME}/${BANK_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PCC_ALIAS}" \
    -file "certs/${PCC_NAME}/${PCC_NAME}.crt" \
    -storepass "${ZUUL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${ZUUL_NAME}/keystore/${ZUUL_NAME}.truststore.p12" || exit
echo "truststore zuul - done"

#Into CLOUD
# Import Root,TLS,eureka
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${CLOUD_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${CLOUD_NAME}/keystore/${CLOUD_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${CLOUD_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${CLOUD_NAME}/keystore/${CLOUD_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${CLOUD_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${CLOUD_NAME}/keystore/${CLOUD_NAME}.truststore.p12"  || exit
echo "truststore cloud - done"

#Into LITERARY-SOCIETY
# Import Root,TLS,eureka,cloud,client,zuul,gateway,paypal,bitcoin,bank,pcc
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${GATEWAY_ALIAS}" \
    -file "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PAYPAL_ALIAS}" \
    -file "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BITCOIN_ALIAS}" \
    -file "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BANK_ALIAS}" \
    -file "certs/${BANK_NAME}/${BANK_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PCC_ALIAS}" \
    -file "certs/${PCC_NAME}/${PCC_NAME}.crt" \
    -storepass "${LITERARY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${LITERARY_NAME}/keystore/${LITERARY_NAME}.truststore.p12" || exit
echo "truststore literary society - done"
#Into GATEWAY
# Import Root,TLS,eureka,cloud,client,zuul,literary,paypal,bitcoin,bank,pcc
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${LITERARY_ALIAS}" \
    -file "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PAYPAL_ALIAS}" \
    -file "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BITCOIN_ALIAS}" \
    -file "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BANK_ALIAS}" \
    -file "certs/${BANK_NAME}/${BANK_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PCC_ALIAS}" \
    -file "certs/${PCC_NAME}/${PCC_NAME}.crt" \
    -storepass "${GATEWAY_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${GATEWAY_NAME}/keystore/${GATEWAY_NAME}.truststore.p12" || exit
echo "truststore gateway - done"

#Into PAYPAL
# Import Root,TLS,eureka,cloud,client,zuul,literary,gateway
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/${PAYPAL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${LITERARY_ALIAS}" \
    -file "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${GATEWAY_ALIAS}" \
    -file "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -storepass "${PAYPAL_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PAYPAL_NAME}/keystore/${PAYPAL_NAME}.truststore.p12" || exit

echo "truststore paypal - done"

#Into BITCOIN
# Import Root,TLS,eureka,cloud,client,zuul,literary,gateway
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/${BITCOIN_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${LITERARY_ALIAS}" \
    -file "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${GATEWAY_ALIAS}" \
    -file "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -storepass "${BITCOIN_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BITCOIN_NAME}/keystore/${BITCOIN_NAME}.truststore.p12" || exit

echo "truststore bitcoin - done"

#Into BANK
# Import Root,TLS,eureka,cloud,client,zuul,literary,gateway,pcc
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/${BANK_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${LITERARY_ALIAS}" \
    -file "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${GATEWAY_ALIAS}" \
    -file "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${PCC_ALIAS}" \
    -file "certs/${PCC_NAME}/${PCC_NAME}.crt" \
    -storepass "${BANK_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${BANK_NAME}/keystore/${BANK_NAME}.truststore.p12" || exit

echo "truststore bank - done"


#Into PCC
# Import Root,TLS,eureka,cloud,client,zuul,literary,gateway,bank
keytool \
    -importcert \
    -noprompt \
    -alias "root" \
    -file ca/root-ca.crt \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/${PCC_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "tls" \
    -file ca/tls-ca.crt \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${EUREKA_ALIAS}" \
    -file "certs/${EUREKA_NAME}/${EUREKA_NAME}.crt" \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLOUD_NAME}" \
    -file "certs/${CLOUD_NAME}/${CLOUD_NAME}.crt" \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12"  && \
keytool \
    -importcert \
    -noprompt \
    -alias "${CLIENT_ALIAS}" \
    -file "certs/${CLIENT_NAME}/${CLIENT_NAME}.crt" \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${ZUUL_ALIAS}" \
    -file "certs/${ZUUL_NAME}/${ZUUL_NAME}.crt" \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${LITERARY_ALIAS}" \
    -file "certs/${LITERARY_NAME}/${LITERARY_NAME}.crt" \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${GATEWAY_ALIAS}" \
    -file "certs/${GATEWAY_NAME}/${GATEWAY_NAME}.crt" \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12" || exit
keytool \
    -importcert \
    -noprompt \
    -alias "${BANK_ALIAS}" \
    -file "certs/${BANK_NAME}/${BANK_NAME}.crt" \
    -storepass "${PCC_KEYSTORE_PASSWORD}" \
    -storetype "PKCS12" \
    -keystore "certs/${PCC_NAME}/keystore/${PCC_NAME}.truststore.p12" || exit

echo "truststore pcc - done"
