package util;

import play.Play;

public class Config {

	public static String getConfig(String value) throws Exception{
		try{
			return Play.application().configuration().getString(value);
		}catch(Exception e){
			throw new Exception("Falied to get configuration value");
		}
	}
	
	public static String getHost() throws Exception{
		return getConfig(Constants.HOST_PROP);
	}
	
	public static String getPort() throws Exception{
		return getConfig(Constants.PORT_PROP);
	}
	
	public static String getMongoHost() throws Exception{
		return getConfig(Constants.MONGO_HOST_PROP);
	}

	public static int getMongoPort() throws Exception{
		return Integer.parseInt(getConfig(Constants.MONGO_PORT_PROP));
	}

    public static String getMastercardPrivateKey() throws Exception{
        return getConfig(Constants.MASTERCARD_PRIVATE_KEY_PROP);
    }

    public static String getMastercardPublicKey() throws Exception{
        return getConfig(Constants.MASTERCARD_PUBLIC_KEY_PROP);
    }
	
	public static String getMongoDB() throws Exception{
		return getConfig(Constants.MONGO_DB_PROP);
	}
}
