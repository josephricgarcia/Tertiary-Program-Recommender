package com.example.tertiaryprogramreccommender

class RecommendationController(private val model: RecommendationModel) {

    fun getUserScores(callback: (Map<String, Pair<Int, Int>>) -> Unit) {
        model.fetchUserScores { scores ->
            callback(scores)
        }
    }

    fun getRecommendedPrograms(callback: (List<Pair<String, Double>>) -> Unit) {
        model.fetchUserScores { scores ->
            model.calculateProgramRanking(scores) { programs ->
                callback(programs)
            }
        }
    }
}
