package com.sortisplus.data.local

import android.content.Context
import com.sortisplus.core.data.DatabaseResult
import com.sortisplus.core.data.Element
import com.sortisplus.core.data.ElementRepository
import com.sortisplus.core.data.Persona
import com.sortisplus.core.data.PersonaRepository
import com.sortisplus.core.data.SettingsRepository
import com.sortisplus.core.database.DatabaseProvider
import com.sortisplus.core.database.model.ElementEntity
import com.sortisplus.core.database.model.PersonaEntity
import com.sortisplus.core.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalElementRepository(context: Context) : ElementRepository {
    private val db = DatabaseProvider.get(context)
    private val dao = db.elementDao()

    override fun observeAll(): Flow<List<Element>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun create(title: String): Long {
        val now = System.currentTimeMillis()
        return dao.insert(ElementEntity(title = title, createdAt = now))
    }

    override suspend fun update(id: Long, title: String): Boolean {
        val current = dao.getById(id) ?: return false
        val changed = current.copy(title = title, updatedAt = System.currentTimeMillis())
        return dao.update(changed) > 0
    }

    override suspend fun delete(id: Long): Boolean =
        dao.deleteById(id) > 0
}

private fun ElementEntity.toDomain() = Element(
    id = id,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt
)

class LocalPersonaRepository(context: Context) : PersonaRepository {
    private val db = DatabaseProvider.get(context)
    private val dao = db.personaDao()

    override fun observeAll(): Flow<List<Persona>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): DatabaseResult<Persona?> = try {
        val persona = dao.getById(id)?.toDomain()
        DatabaseResult.Success(persona)
    } catch (e: Exception) {
        DatabaseResult.Error(e)
    }

    override suspend fun create(
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): DatabaseResult<Long> {
        return try {
            // Validate input
            val validation = Persona.validate(nombre, apellido, fechaNacimiento, peso)
            if (!validation.isValid) {
                return DatabaseResult.Error(
                    IllegalArgumentException(validation.errors.joinToString(", "))
                )
            }
            
            val id = dao.insert(
                PersonaEntity(
                    nombre = nombre.trim(),
                    apellido = apellido.trim(),
                    fechaNacimiento = fechaNacimiento,
                    peso = peso,
                    esZurdo = esZurdo
                )
            )
            DatabaseResult.Success(id)
        } catch (e: Exception) {
            DatabaseResult.Error(e)
        }
    }

    override suspend fun update(
        id: Long,
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): DatabaseResult<Boolean> {
        return try {
            // Validate input
            val validation = Persona.validate(nombre, apellido, fechaNacimiento, peso)
            if (!validation.isValid) {
                return DatabaseResult.Error(
                    IllegalArgumentException(validation.errors.joinToString(", "))
                )
            }
            
            val current = dao.getById(id) 
                ?: return DatabaseResult.Error(IllegalArgumentException("Persona con ID $id no encontrada"))
                
            val changed = current.copy(
                nombre = nombre.trim(),
                apellido = apellido.trim(),
                fechaNacimiento = fechaNacimiento,
                peso = peso,
                esZurdo = esZurdo
            )
            val success = dao.update(changed) > 0
            DatabaseResult.Success(success)
        } catch (e: Exception) {
            DatabaseResult.Error(e)
        }
    }

    override suspend fun delete(id: Long): DatabaseResult<Boolean> = try {
        val success = dao.deleteById(id) > 0
        if (!success) {
            DatabaseResult.Error(IllegalArgumentException("Persona con ID $id no encontrada"))
        } else {
            DatabaseResult.Success(true)
        }
    } catch (e: Exception) {
        DatabaseResult.Error(e)
    }
}

private fun PersonaEntity.toDomain() = Persona(
    id = id,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    peso = peso,
    esZurdo = esZurdo
)

class LocalSettingsRepository(context: Context) : SettingsRepository {
    private val ds = SettingsDataStore(context)
    override val darkTheme = ds.darkTheme
    override val listOrder = ds.listOrder
    override suspend fun setDarkTheme(value: Boolean) = ds.setDarkTheme(value)
    override suspend fun setListOrder(value: String) = ds.setListOrder(value)
}

object LocalProviders {
    fun elementRepository(context: Context): ElementRepository =
        LocalElementRepository(context)

    fun personaRepository(context: Context): PersonaRepository =
        LocalPersonaRepository(context)

    fun settingsRepository(context: Context): SettingsRepository =
        LocalSettingsRepository(context)
}