# Registro de prompts de la Fase 2

El usuario solicitó la implementación autónoma a Codex. La fila cita fragmentos de un único mensaje real, agrupados porque se dieron juntos. La guía menciona Gemini. Esta ejecución se realizó con Codex.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Te autorizo a iniciar y completar de manera autónoma toda la Fase 2 del Laboratorio 09.» «Utiliza la versión final de MuroCampus-mio, commit `32443c4`, como base para las mejoras.» «Eliminar publicaciones mediante DELETE y Response&lt;Unit&gt;.» «Editar el título de una publicación mediante PATCH.» «Buscar publicaciones por título entre las publicaciones descargadas.» | Codex importó esa versión como base de `codigo-ia`. Implementó DELETE con confirmación y validación de la respuesta HTTP, una pantalla que envía solo el título por PATCH, y la búsqueda local sobre el filtro activo. Compiló y probó las tres mejoras en Pixel_8 y documentó los resultados y las capturas. | Sin cambios o descartes del estudiante registrados. | Sin justificación del estudiante registrada. |

Codex conservó los cambios confirmados en el ViewModel durante la sesión porque JSONPlaceholder no los persiste, y mantuvo la búsqueda sobre el conjunto ya descargado para evitar peticiones al escribir. Esas fueron decisiones de implementación de la IA.
