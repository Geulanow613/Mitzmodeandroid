package com.beardytop.mitzmode.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Singleton
class DailyMitzvotRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("daily_mitzvot_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveChecklistStates(states: Map<String, Boolean>) {
        // Move to background thread to prevent ANR
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val date = LocalDate.now().toString()
                val json = gson.toJson(states)
                
                val success = prefs.edit()
                    .putString("checklist_$date", json)
                    .commit() // Use commit() for synchronous write in background
                    
                if (!success) {
                    android.util.Log.w("DailyMitzvotRepository", "Failed to save checklist states")
                }
            } catch (e: Exception) {
                android.util.Log.e("DailyMitzvotRepository", "Error saving checklist states", e)
            }
        }
    }

    fun getChecklistStates(): Map<String, Boolean> {
        return try {
            val today = LocalDate.now().toString()
            val lastSavedDate = prefs.getString("last_saved_date", "") ?: ""
            
            // If it's a new day, clear the states
            if (lastSavedDate != today) {
                // Move cleanup to background thread
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        prefs.edit()
                            .remove("checklist_$lastSavedDate")
                            .putString("last_saved_date", today)
                            .remove("tzaddik_shown_$lastSavedDate")
                            .commit()
                    } catch (e: Exception) {
                        android.util.Log.e("DailyMitzvotRepository", "Error cleaning up old checklist data", e)
                    }
                }
                return emptyMap()
            }
            
            val json = prefs.getString("checklist_$today", null) ?: return emptyMap()
            try {
                gson.fromJson(json, object : TypeToken<Map<String, Boolean>>() {}.type) ?: emptyMap()
            } catch (e: Exception) {
                android.util.Log.e("DailyMitzvotRepository", "Error parsing checklist JSON", e)
                emptyMap()
            }
        } catch (e: Exception) {
            android.util.Log.e("DailyMitzvotRepository", "Error getting checklist states", e)
            emptyMap()
        }
    }

    fun hasShownTzaddikToday(): Boolean {
        return try {
            val date = LocalDate.now().toString()
            prefs.getBoolean("tzaddik_shown_$date", false)
        } catch (e: Exception) {
            android.util.Log.e("DailyMitzvotRepository", "Error checking tzaddik status", e)
            false
        }
    }

    fun markTzaddikShown() {
        // Move to background thread to prevent ANR
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val date = LocalDate.now().toString()
                val success = prefs.edit()
                    .putBoolean("tzaddik_shown_$date", true)
                    .commit()
                    
                if (!success) {
                    android.util.Log.w("DailyMitzvotRepository", "Failed to mark tzaddik shown")
                }
            } catch (e: Exception) {
                android.util.Log.e("DailyMitzvotRepository", "Error marking tzaddik shown", e)
            }
        }
    }

    fun hasShownBlessedToday(): Boolean {
        return try {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            prefs.getString("last_blessed_date", "") == today
        } catch (e: Exception) {
            android.util.Log.e("DailyMitzvotRepository", "Error checking blessed status", e)
            false
        }
    }

    fun markBlessedShown() {
        // Move to background thread to prevent ANR
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val success = prefs.edit()
                    .putString("last_blessed_date", today)
                    .commit()
                    
                if (!success) {
                    android.util.Log.w("DailyMitzvotRepository", "Failed to mark blessed shown")
                }
            } catch (e: Exception) {
                android.util.Log.e("DailyMitzvotRepository", "Error marking blessed shown", e)
            }
        }
    }
} 