function generateCertificates() {
    rm -rf "/crl"
    rm -rf "/ca"
    rm -rf "/certs"
    ./gen.sh
}

generateCertificates
