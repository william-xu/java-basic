package com.xwl41.common.basic.constant;

public enum Algorithms {
        AES,			//Parameters for use with the AES algorithm.

        Blowfish,		//Parameters for use with the Blowfish algorithm.

        DES,			//Parameters for use with the DES algorithm.

        DESede,			//Parameters for use with the DESede algorithm.

        DiffieHellman,	//Parameters for use with the DiffieHellman algorithm.

        DSA,			//Parameters for use with the Digital Signature Algorithm.

        OAEP,			//Parameters for use with the OAEP algorithm.

        //	PBEWith<digest>And<encryption>	Parameters for use with the PBEWith<digest>And<encryption> algorithm. Examples: PBEWithMD5AndDES, and PBEWithHmacSHA256AndAES_128.
        PBE,			//Parameters for use with the PBE algorithm. This name should not be used, in preference to the more specific PBE-algorithm names previously listed.

        RC2,			//Parameters for use with the RC2 algorithm.

        RSA,			//The RSA encryption algorithm as defined in PKCS #1

        HmacMD5,

        //CertPathBuilder Algorithms
        PKIX

    }