package com.pamn.ggmatch.app.riotApi

import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.riotApi.dto.LeagueEntryDto
import com.pamn.ggmatch.app.riotApi.dto.RiotAccountDto
import com.pamn.ggmatch.app.riotApi.dto.SummonerDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class OkHttpRiotApiClient(
    private val http: OkHttpClient,
    private val apiKey: String,
) : RiotApiClient {

    // ✅ EUW fixed
    private val accountHost = "https://europe.api.riotgames.com"
    private val lolHost = "https://euw1.api.riotgames.com"

    private fun request(url: String): Request =
        Request.Builder()
            .url(url)
            .header("X-Riot-Token", apiKey)
            .get()
            .build()

    override suspend fun getPuuidByRiotId(
        gameName: String,
        tagLine: String,
    ): Result<RiotAccountDto, AppError> =
        withContext(Dispatchers.IO) {
            runCatching {
                val gn = URLEncoder.encode(gameName.trim(), "UTF-8")
                val tl = URLEncoder.encode(tagLine.trim(), "UTF-8")
                val url = "$accountHost/riot/account/v1/accounts/by-riot-id/$gn/$tl"

                http.newCall(request(url)).execute().use { res ->
                    val body = res.body?.string().orEmpty()

                    if (!res.isSuccessful) {
                        return@withContext Result.Error(
                            AppError.Unexpected("Riot account lookup failed (HTTP ${res.code}): $body"),
                        )
                    }

                    val json = JSONObject(body)
                    print(json.getString("puuid"))
                    Result.Ok(RiotAccountDto(puuid = json.getString("puuid")))
                }
            }.getOrElse { e ->
                Result.Error(AppError.Unexpected("Riot account lookup failed", e))
            }
        }

    override suspend fun getLeagueEntriesByPuuid(
        puuid: String,
    ): Result<List<LeagueEntryDto>, AppError> =
        withContext(Dispatchers.IO) {
            runCatching {
                val url = "$lolHost/lol/league/v4/entries/by-puuid/$puuid"

                http.newCall(request(url)).execute().use { res ->
                    val body = res.body?.string().orEmpty()

                    if (!res.isSuccessful) {
                        return@withContext Result.Error(
                            AppError.Unexpected("League entries by puuid failed (HTTP ${res.code}): $body"),
                        )
                    }

                    val arr = JSONArray(body)
                    val out = mutableListOf<LeagueEntryDto>()

                    for (i in 0 until arr.length()) {
                        val o = arr.getJSONObject(i)
                        out += LeagueEntryDto(
                            queueType = o.optString("queueType"),
                            tier = o.optString("tier"),
                            rank = o.optString("rank").takeIf { it.isNotBlank() },
                            leaguePoints = o.optInt("leaguePoints"),
                            wins = o.optInt("wins"),
                            losses = o.optInt("losses"),
                            hotStreak = o.optBoolean("hotStreak"),
                            inactive = o.optBoolean("inactive"),
                        )
                    }

                    Result.Ok(out)
                }
            }.getOrElse { e ->
                Result.Error(AppError.Unexpected("League entries by puuid failed", e))
            }
        }

}
