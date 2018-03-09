package com.yatang.xc.xcr.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Keys  implements Serializable {
	private static final long serialVersionUID = 1L;
	private String publicKey;
	private String privateKey;
	private String desKey;

	public String getDesKey() {
		return desKey;
	}

	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		return "Key [publicKey=" + publicKey + ", privateKey=" + privateKey + ", desKey=" + desKey + "]";
	}
}