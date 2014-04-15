package gov.hhs.fha.nhinc.directconfig.dao;

import java.util.Collection;

import gov.hhs.fha.nhinc.directconfig.entity.Setting;

/**
 * Settings data access methods.
 */
public interface SettingDao
{
    /**
     * Get all settings.
     * @return All settings
     */
    public Collection<Setting> getAll();


    /**
     * Get a collection of settings by names.
     * @param names The names to search for.
     * @return A collection of settings matching the names.
     */
    public Collection<Setting> getByNames(Collection<String> names);

    /**
     * Adds a new setting.  Fails if the setting already exists.
     * @param name The name of the setting to add.
     * @param value The value of the setting to add.
     */
    public void add(String name, String value);


    /**
     * Updates a setting.
     * @param name The name of the setting to update.
     * @param value The value of the setting to update.
     */
    public void update(String name, String value);

    /**
     * Delete a set of settings with the given names.
     * @param names The names of the settings to delete.
     */
    public void delete(Collection<String> names);
}
