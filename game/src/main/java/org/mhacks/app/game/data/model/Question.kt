package org.mhacks.app.game.data.model

data class Question(
	val taskFormat: String,
	val name: String,
	val options: List<String>,
	val text: String
)
