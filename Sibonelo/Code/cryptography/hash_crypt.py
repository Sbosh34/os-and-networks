import hashlib, binascii

sha3_256_sibonelo_hash = hashlib.sha3_256(b'sibonelo').digest()
sha3_224_siboneli_hash = hashlib.sha3_256(b'siboneli').digest()
sha3_224_sibonelo_hash = hashlib.sha3_224(b'sibonelo').digest()

sha3_224_ntokozo_hash = hashlib.sha3_224(b'ntokozo').digest()
sha3_256_ntokozo_hash = hashlib.sha3_256(b'ntokozo').digest()
sha3_256_ntokoze_hash = hashlib.sha3_256(b'ntokoze').digest()


print("Sibonelo - SHA3_256 =", binascii.hexlify(sha3_256_sibonelo_hash))
print("Sibonelo - SHA3_224 =", binascii.hexlify(sha3_224_sibonelo_hash))
print("Siboneli - SHA3_224 =", binascii.hexlify(sha3_224_siboneli_hash))


print("Ntokozo  - SHA3_256 =", binascii.hexlify(sha3_256_ntokozo_hash))
print("Ntokozo  - SHA3_224 =", binascii.hexlify(sha3_224_ntokozo_hash))


