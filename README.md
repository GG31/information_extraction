README

Auteur : Geneviève CIRERA, SI5- WEB
===============================================================

ParserEntity lit le fichier des entity et les crée. En les lisant, il ajoute les types qui peuvent être trouvés dans les noms des entités.
Par exemple 
   - Claude_Bertrand_(actor), Claude_Bertrand est un actor.
   - Ayase,_Kanagawa, Ayase Kanagawa est une city.
Lors du parsing du fichier des entity, les objets Entity sont créé avec les mots clés (identifiants) qui serviront à reconnaitre les entités dans le texte. Par exemple Claude_Bertrand aura pour identifiant Claude et Bertrand.
De plus, grâce à une base de données prédéfini chaque entité peut avoir un identifiant supplémentaire le mot clé "she", "he" ou "it" suivant le cas. Ainsi lors du parsing d'une page wikipedia, on suppose que lorsqu'on parle de "she", "he" ou "it" il s'agit de l'entité de la page en lecture ce qui est, dans la grande majorité des cas, vrai.

Ensuite, ParseEntity lit tous les fichiers du dossier Wikipedia_corpus et essaye de trouver les entity pour y mettre les balises. Pour chaque ligne, la ligne est splitté par un espace et les mots sont analysés du 0 à la fin de ligne par checkWords.
checkWords est une méthode récursive, qui essaye de trouver le plus de mots d'affilés correspondants aux identifiants. Elle renvoie le nombre de mots trouvés ainsi les balises peuvent être mise en conséquence. Si jamais il n'y qu'un mot de trouvé, une analyse du mot précédent et suivant est effectuée (avec checkWordAfterBefore). Cette méthode consiste à vérifier que le mot d'avant ou après de la même phrase ne commence pas par une majuscule. En effet, si c'est le cas c'est que l'on parle peut-être de Catherine Goux et non de Catherine Deneuve.

Si aucune entité n'est trouvé c'est qu'il s'agit peut-être d'une date ou d'un type. Les classe DateExtrator et TypeExtrator extrait respectivement les dates de naissance et les types de l'entité en question.

Chaque fichier analysé est réécrit dans le dossier "Sortie" avec les balises.

DateExtractor essaye de trouver le mot born suivit d'un nombre, un mois et une années ou des parenthèses où il y aurait une ou plusieurs date.
Si elle trouve un de ses cas, elle formalise la date au format AAAA-MM-JJ et crée un triplet dans l'entité avec la propriété "hasDate" et la date de naissance.

TypeExtractor retrouve le type de l'entité. Pour cela, la classe se sert d'un pattern d'expression régulière. J'ai défini dans une arraylist, toutes les possiblités pour exprimer le type ("is a", "is an", "was a", "was an"). Qui se remplacera dans l'expression régulière :".*entity name=\"(\\D+)\".*entity(\\D+) " + "is a" + " (\\D+)"
On pourra récupérer l'uri, la potentielle chaine de caractères qui suit "entity" et tout ce qui suit "is a" après l'application du pattern dans chaque phrase des pages wikipedia. S'il matche, premièrement on vérifie qu'il n'y a pas de "and" dans la deuxième chaine qu'on récupère car ça voudrait dire qu'on ne parle plus de la même entité. 
Ensuite on analyse ce qui suit "is a", "is an", "was a" ou "was an". L'uri de l'entité en question est récupérée lors de l'application du pattern, ce qui permet de former un triplet.
L'analyse considère que si on a "actor, photographer and painter" cela équivaut à 3 types différents et donc 3 triplets.
Si la phrase est "is an actor who", "who" est considéré comme délimitateur, on ne conservera que "actor" dans le triplet. D'autres délimitateur sont définis en paramètre tels que "from", "of", "where"...

La classe PatternExtractor se charge de trouver les patterns entre deux entités. Pour cela, les fichiers de "Sortie" sont lus afin d'avoir les fichiers avec les balises entity.
Dans cette classe les patterns à rechercher sont dans une hashmap avec pour clé le nom de la relation et pour valeur une arraylist avec toutes les possibilités pour exprimer cette relation.
On sait que toutes ces relations sont entre deux entités, donc on aura quelque chose du type <entity name="uri1">Personne1</entity> is married to <entity name="uri2">Personne2</entity>
Pour récupérer les uris encadrants "is married to" j'ai utilisé une expression régulière : ".*entity name=\"(\\D+)\".*entity. "+ "is married to" +" .entity name=\"(\\D+)\"..*"
En remplaçant "is married to" par toutes les valeurs de l'arraylist de la hashmap correspondant à la clé marriedTo, j'obtiens directement les uris désirées. Il ne reste plus qu'à créer le triplet associé.
Cette méthode est générique, il suffit d'ajouter dans la HashMap les relations et mots clés voulu, et les triplets se créront automatiquement sans autre modification du code.
