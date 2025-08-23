package com.androidbase.data.local

import android.content.Context
import com.androidbase.core.data.Element
import com.androidbase.core.data.ElementRepository
import com.androidbase.core.data.Persona
import com.androidbase.core.data.PersonaRepository
import com.androidbase.core.data.SettingsRepository
import com.androidbase.core.database.DatabaseProvider
import com.androidbase.core.database.model.ElementEntity
import com.androidbase.core.database.model.PersonaEntity
import com.androidbase.core.datastore.SettingsDataStore
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

    override suspend fun getById(id: Long): Persona? = dao.getById(id)?.toDomain()

    override suspend fun create(
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): Long {
        return dao.insert(
            PersonaEntity(
                nombre = nombre,
                apellido = apellido,
                fechaNacimiento = fechaNacimiento,
                peso = peso,
                esZurdo = esZurdo
            )
        )
    }

    override suspend fun update(
        id: Long,
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): Boolean {
        val current = dao.getById(id) ?: return false
        val changed = current.copy(
            nombre = nombre,
            apellido = apellido,
            fechaNacimiento = fechaNacimiento,
            peso = peso,
            esZurdo = esZurdo
        )
        return dao.update(changed) > 0
    }

    override suspend fun delete(id: Long): Boolean = dao.deleteById(id) > 0
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