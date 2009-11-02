/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/repository/filesystem/FileSystemRepositoryManager.java,v 1.27 2006/08/24 20:42:34 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.repository.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.RepositoryItem;
import org.freebxml.omar.common.RepositoryItemImpl;
import org.freebxml.omar.common.Utility;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.repository.AbstractRepositoryManager;
import org.freebxml.omar.server.repository.RepositoryItemKey;
import org.freebxml.omar.server.repository.RepositoryManager;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.w3c.dom.Element;


/**
* FileSystem-based Repository Manager to control inserting, updating
* and deleting repository items.
*
* @author Adrian Chong
* @author Peter Burgess
*
* @version $Version: $ [$Date: 2006/08/24 20:42:34 $]
*/
public class FileSystemRepositoryManager extends AbstractRepositoryManager {
    /** Singleton instance of RepositoryManager */
    private static RepositoryManager instance = null;
    private static final Log log = LogFactory.getLog(FileSystemRepositoryManager.class);
    private String repositoryRoot;

    protected FileSystemRepositoryManager() {
        repositoryRoot =
	    RegistryProperties.getInstance().getProperty("omar.repository.home");
    }

    /**
     * Gets the path for a RepositoryItem given its id.
     */
    private String getRepositoryItemPath(String id) throws RegistryException {
        //Strip urn:uuid since that is not part of file name
        id = Utility.getInstance().stripId(id);

        String repositoryItemPath = repositoryRoot + "/" + id;

        return repositoryItemPath;
    }

    /**
     * Gets the RepositoryItem as a stream of XML markup given its id.
     *
     * @param id Unique id for repository item
     * @return a <code>StreamSource</code> value
     * @exception RegistryException if an error occurs
     */
    public StreamSource getAsStreamSource(String id) throws RegistryException {
	StreamSource riFileSrc = null;

	try {
	    File riFile = new File(getRepositoryItemPath(id));
	    riFileSrc = new StreamSource(new FileInputStream(riFile));
	} catch (Exception e) {
	    throw new RegistryException(e);
	}

	return riFileSrc;
    }

    /**
    * Singleton instance accessor.
    */
    public synchronized static RepositoryManager getInstance() {
        if (instance == null) {
            instance = new FileSystemRepositoryManager();
        }

        return instance;
    }

    /**
    * Insert the repository item.
    * @param fileName It should be the UUID. It will remove "urn:uuid:".
    * @param item The repository item.
    */
    public void insert(ServerRequestContext context, RepositoryItem item) throws RegistryException {
        try {
            String id = item.getId();

            // Strip off the "urn:uuid:"
            id = Utility.getInstance().stripId(id);

            String itemPath = getRepositoryItemPath(id);

            log.debug("itemPath = " + itemPath);

            File itemFile = new File(itemPath);

            if (itemFile.exists()) {
                String errmsg = ServerResourceBundle.getInstance().getString("message.RepositoryItemWithIdAlreadyExists",
				                                                    new Object[]{item.getId()});
                log.error(errmsg);
                throw new RegistryException(errmsg);
            }

            //Writing out the RepositoryItem itself			
            FileOutputStream fos = new FileOutputStream(itemPath);
            item.getDataHandler().writeTo(fos);
            fos.flush();
            fos.close();

            // Writing out the ri 's signature
            // if it exists.
            Element sigElement = item.getSignatureElement();

            File itemSig = new File(getRepositoryItemPath(id) + ".sig");

            if (itemSig.exists()) {
                String errmsg =ServerResourceBundle.getInstance().getString("message.PayloadSignatureAlreadyExists",
				                                                  new Object[]{item.getId()});
                log.error(errmsg);
                throw new RegistryException(errmsg);
            }

            FileOutputStream sigFos = new FileOutputStream(itemSig);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(sigElement), new StreamResult(sigFos));

            sigFos.flush();
            sigFos.close();

        } catch (Exception e) {
            log.error(e);
            throw new RegistryException(e);
        }
    }

    /**
    * Returns the RepositoryItem with the given unique ID.
    *
    * @param id Unique id for repository item
    * @return RepositoryItem instance
    * @exception RegistryException
    */
    public RepositoryItem getRepositoryItem(String id)
        throws RegistryException {
        RepositoryItem repositoryItem = null;
        String origId = id;

        // Strip off the "urn:uuid:"
        id = Utility.getInstance().stripId(id);

        try {
            String path = getRepositoryItemPath(id);
            File riFile = new File(path);

            if (!riFile.exists()) {
                String errmsg = ServerResourceBundle.getInstance().getString("message.RepositoryItemDoesNotExist",
				                                                   new Object[]{id});
                log.error(errmsg);
                throw new RegistryException(errmsg);
            }

            DataHandler contentDataHandler = new DataHandler(new FileDataSource(
                        riFile));

            Element sigElement = null;
            path += ".sig";

            File sigFile = new File(path);

            if (!sigFile.exists()) {
                String errmsg = "Payload signature for repository item id=\"" +
                    id + "\" does not exist!";
                //log.error(errmsg);
                
                throw new RegistryException(errmsg);
            } else {
                
                javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
                org.w3c.dom.Document sigDoc = db.parse(new FileInputStream(sigFile));

                repositoryItem = new RepositoryItemImpl(id, sigDoc.getDocumentElement(),
                    contentDataHandler);                
            }

        } catch (RegistryException e) {
            throw e;
        } catch (Exception e) {
            throw new RegistryException(e);
        }

        return repositoryItem;
    }

    /**
    * Delete the repository item.
    * @param id It should be the UUID. It will remove "urn:uuid:".
    * @throws RegistryException if the item does not exist
    */
    public void delete(String id) throws RegistryException {
        // Strip off the "urn:uuid:"
        id = Utility.getInstance().stripId(id);

        String path = getRepositoryItemPath(id);

        log.debug("path = " + path);

        //RepositoryItem file
        File riFile = new File(path);

        //RepositoryItem XML signature file
        File riSigFile = new File(path + ".sig");

        if (!riFile.exists()) {
            String errmsg = ServerResourceBundle.getInstance().getString("message.RepositoryItemDoesNotExist",
				                                                   new Object[]{id});
            log.error(errmsg);
            throw new RegistryException(errmsg);
        }

        boolean deletedOK = riFile.delete();

        if (riSigFile.exists()) {
            deletedOK = riSigFile.delete();

            if (!deletedOK) {
                //log error
                String errmsg = ServerResourceBundle.getInstance().getString("message.CannotDeletePayloadSignature",
				                                                   new Object[]{id});
                log.error(errmsg);
            }
        }

        if (deletedOK) {
            String msg = "deleted OK";
            log.debug(msg);
        } else {
            String msg = ServerResourceBundle.getInstance().getString("message.deletedNOK");
            log.error(msg);
        }
    }

    /**
    * Return a List of non-existent repository items
    * @param ids The List of repository items id. It will remove "urn:uuid:".
    */
    public List itemsExist(List ids) throws RegistryException {
        List notExistIds = new java.util.ArrayList();
        Iterator iter = ids.iterator();

        while (iter.hasNext()) {
            String id = (String) iter.next();

            // Strip off the "urn:uuid:"
            id = Utility.getInstance().stripId(id);

            File f = new File(getRepositoryItemPath(id));

            if (!f.isFile()) {
                notExistIds.add("urn:uuid:" + id);
            }
        }

        return notExistIds;
    }

    /**
    * Determines if RepositoryItem exists for specified key. 
    *
    * @return true if a RepositoryItem exists for specified key, false otherwise 
    * @param key The RepositoryItemKey.
    */
    public boolean itemExists(RepositoryItemKey key) throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unimplemented"));
    }
    
    /**
    * Get the size of a repository item in bytes.
    * @param itemId The id of repository item, with "urn:uuid:".
    * @return 0 if the file does not exist.
    */
    public long getItemSize(String itemId) throws RegistryException {
        File f = new File(getRepositoryItemPath(itemId));

        return f.length();
    }
        
    public void delete(org.freebxml.omar.server.repository.RepositoryItemKey key) throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unimplemented"));
    }
    
    public RepositoryItem getRepositoryItem(org.freebxml.omar.server.repository.RepositoryItemKey key) throws RegistryException {
        throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unimplemented"));
    }
    
}
