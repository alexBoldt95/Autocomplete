An autocompleter program that simply finds matching strings based on what the user has input from a database.

***INSTRUCTIONS***
Run the AutocompleteMain class.
Simply type in a string and watch the magic happen!

You can change the type of data structure the program uses to search by modifying the line 22 of AutocompleteMain
and changing it to one of the structures found in lines 17-19. TRIE_AUTOCOMPLETE is the fastest, using a massive
trie to store all strings. BRUTE_AUTOCOMPLETE manually searches the entire database every time a new character is
input. BINARY_SEARCH_AUTOCOMPLETE uses binary searching to find a range of matching strings.
***


Name: Alex Boldt
NetID: apb34
Hours Spent: 10 hours
Consulted With: Elizabeth Zhao (ez18), Ademola Olayinka (aoo12)
Resources Used: Java API HTML docs
Impressions: Good exercise on priority queues, binary search and using comparators.
----------------------------------------------------------------------
Problem 1: What is the order of growth (big-Oh) of the number of compares
(in the worst case) that each of the operations in the Autocomplete data
type make, as a function of the number of terms N, the number of matching
terms M, and k, the number of matches returned by topKMatches for
BinarySearchAutocomplete?

BruteAutocomplete: Number of compares is O(N)always because it compares all of the terms pairwise to find the
top K as well as the top.

BinarySearchAutocomplete: It takes O(log N) to find both the first and last index of matching terms. Then in
the priority queue where all of the matching terms must be compared, there are O(M log M) comparisons to keep
the queue sorted. So in total, there are O(log N + M log M) comparisons.

TrieAutocomplete: Once the trie is built up, not much needs to be done. Comparisons only need to be done between
matching terms (because they will all be subtrees of the prefix, so we can ignore all of the other N terms),
and keeping M nodes organized in a priority queue takes O(M log M) time. So O(M log M) comparisons.


Problem 2: How does the runtime of topKMatches() vary with k, assuming a fixed prefix
and set of terms? 
2A BruteAutocomplete: Doesn't vary with k, because all the terms will be looked at always.

2B BinarySearchAutocomplete: Varies with with k linearly because as more results are needed, more results need
to be placed into the final String array to return.

2C TrieAutocomplete: Varies linearly with k because at most k words need to be put in the final String array
to return.

Problem 3: Look at the methods topMatch and topKMatches in BruteAutocomplete and
BinarySearchAutocomplete and compare both their theoretical and empirical
runtimes. Is BinarySearchAutocomplete always guaranteed to perform better
than BruteAutocomplete? Justify your answer.

BruteAutocomplete should be O(N) for both topMatch and topKMatches because they both iterate over the entire 
terms list. 
BSA should be O(log N + M) for topMatch because no sorting is needed and only M matches need to be
iterated over to find the max weighted word. BSA topKMatches should O(log N + M log M) because the sorting of the Priority
Queue will take M log M and 2 log N binary searches must be performed to find the first and last index of the 
matching terms.

Brute will run faster than BSA in cases where the prefix given is the empty string. In these cases, BSA will
perform a binary search on the entire list (because everything in the list matches the empty prefix),
which is slower than just iterating through the entire list once, which is what brute always does. 
BSA will run faster in all other cases, though.

***Where Brute runs faster than BSA

Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\words-333333.txt.
Benchmarking BruteAutocomplete...
Found 333333 words
Time to initialize - 0.028265693
***Time for topMatch("") - 0.006619124977513227***
Time for topMatch("khombu") - 0.01405336181792717
Time for topMatch("k") - 0.009935549228174603
Time for topMatch("kh") - 0.00966584871042471
Time for topMatch("notarealword") - 0.0078179644078125
***Time for topKMatches("", 1) -  0.009492091121442125***
***Time for topKMatches("", 4) -  0.009419948736346516***
***Time for topKMatches("", 7) -  0.009605841677543187***
Time for topKMatches("khombu", 1) -  0.01309743764921466
Time for topKMatches("khombu", 4) -  0.0115639292147806
Time for topKMatches("khombu", 7) -  0.00753436556626506
Time for topKMatches("k", 1) -  0.006117154207823961
Time for topKMatches("k", 4) -  0.008886598111900533
Time for topKMatches("k", 7) -  0.009872473662721893
Time for topKMatches("kh", 1) -  0.009391264577861163
Time for topKMatches("kh", 4) -  0.009306099756505577
Time for topKMatches("kh", 7) -  0.009181071796330275
Time for topKMatches("notarealword", 1) -  0.008075640656451612
Time for topKMatches("notarealword", 4) -  0.008218737701149425
Time for topKMatches("notarealword", 7) -  0.00813317402601626


Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\words-333333.txt.
Benchmarking BinarySearchAutocomplete...
Found 333333 words
Time to initialize - 1.766366822
***Time for topMatch("") - 0.016668620373333335***
Time for topMatch("khombu") - 1.6000571E-5
Time for topMatch("k") - 3.90377186E-4
Time for topMatch("kh") - 2.2087485E-5
Time for topMatch("notarealword") - 1.0076794E-5
***Time for topKMatches("", 1) -  0.0440463906754386***
***Time for topKMatches("", 4) -  0.04341699876724138***
***Time for topKMatches("", 7) -  0.041968838141666666***
Time for topKMatches("khombu", 1) -  5.807635E-6
Time for topKMatches("khombu", 4) -  5.560765E-6
Time for topKMatches("khombu", 7) -  5.556984E-6
Time for topKMatches("k", 1) -  0.001129698615
Time for topKMatches("k", 4) -  0.001151819052
Time for topKMatches("k", 7) -  0.001113731536
Time for topKMatches("kh", 1) -  1.9890513E-5
Time for topKMatches("kh", 4) -  2.8584408E-5
Time for topKMatches("kh", 7) -  3.1960626E-5
Time for topKMatches("notarealword", 1) -  4.402047E-6
Time for topKMatches("notarealword", 4) -  4.342626E-6
Time for topKMatches("notarealword", 7) -  2.476253E-6



Problem 4: For all three of the Autocompletor implementations, how does increasing the
size of the source and increasing the size of the prefix argument affect
the runtime of topMatch and topKMatches? 
4A BruteAutocomplete:

Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\fourletterwordshalf.txt.
Benchmarking BruteAutocomplete...
Found 228488 words
Time to initialize - 0.123039093
Time for topMatch("") - 0.003019733857
Time for topMatch("aenk") - 0.005734024248853211
Time for topMatch("a") - 0.005569791139198218
Time for topMatch("ae") - 0.0052231476221294365
Time for topMatch("notarealword") - 0.003849638934
Time for topKMatches("", 1) -  0.005303262069989396
Time for topKMatches("", 4) -  0.003390285352
Time for topKMatches("", 7) -  0.004637981962
Time for topKMatches("aenk", 1) -  0.007169929171919771
Time for topKMatches("aenk", 4) -  0.0067863113500678425
Time for topKMatches("aenk", 7) -  0.006573500102496715
Time for topKMatches("a", 1) -  0.00441373575
Time for topKMatches("a", 4) -  0.003968673022
Time for topKMatches("a", 7) -  0.005035467048338369
Time for topKMatches("ae", 1) -  0.008571574539383562
Time for topKMatches("ae", 4) -  0.0050423956875
Time for topKMatches("ae", 7) -  0.004234810262
Time for topKMatches("notarealword", 1) -  0.002994108635
Time for topKMatches("notarealword", 4) -  0.003255001123
Time for topKMatches("notarealword", 7) -  0.00321101252

Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\fourletterwords.txt.
Benchmarking BruteAutocomplete...
Found 456976 words
Time to initialize - 0.043028138
Time for topMatch("") - 0.008470179888513514
Time for topMatch("nenk") - 0.014038893070028011
Time for topMatch("n") - 0.009881967391304348
Time for topMatch("ne") - 0.011360568911564626
Time for topMatch("notarealword") - 0.010565594046413503
Time for topKMatches("", 1) -  0.014148410279661017
Time for topKMatches("", 4) -  0.014712658620588235
Time for topKMatches("", 7) -  0.012799411887468031
Time for topKMatches("nenk", 1) -  0.018326910516483517
Time for topKMatches("nenk", 4) -  0.020801994564315352
Time for topKMatches("nenk", 7) -  0.013545456548648648
Time for topKMatches("n", 1) -  0.017605784070422535
Time for topKMatches("n", 4) -  0.013531872835135135
Time for topKMatches("n", 7) -  0.021159298738396625
Time for topKMatches("ne", 1) -  0.01370345933150685
Time for topKMatches("ne", 4) -  0.010185246729124237
Time for topKMatches("ne", 7) -  0.011481923823394495
Time for topKMatches("notarealword", 1) -  0.01450426055652174
Time for topKMatches("notarealword", 4) -  0.01529818833027523
Time for topKMatches("notarealword", 7) -  0.014474613765895953


Doubling the source size doubled all of the runtimes of all the match methods. However, changing the size of the
prefix doesn't really affect runtime since the prefix does not change how many terms are looked at in Brute.

4B BinarySearchAutocomplete:

Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\fourletterwordshalf.txt.
Benchmarking BinarySearchAutocomplete...
Found 228488 words
Time to initialize - 0.070977277
Time for topMatch("") - 0.0069496010541666665
Time for topMatch("aenk") - 2.1098929E-5
Time for topMatch("a") - 7.47822474E-4
Time for topMatch("ae") - 3.576954E-5
Time for topMatch("notarealword") - 1.6153445E-5
Time for topKMatches("", 1) -  0.030554374006097562
Time for topKMatches("", 4) -  0.020035812756
Time for topKMatches("", 7) -  0.013909182208333334
Time for topKMatches("aenk", 1) -  1.085252E-6
Time for topKMatches("aenk", 4) -  2.658838E-6
Time for topKMatches("aenk", 7) -  1.674064E-6
Time for topKMatches("a", 1) -  0.001200454952
Time for topKMatches("a", 4) -  0.002059548915
Time for topKMatches("a", 7) -  0.00211058004
Time for topKMatches("ae", 1) -  4.4419139E-5
Time for topKMatches("ae", 4) -  5.331993E-5
***Time for topKMatches("ae", 7) -  5.8104435E-5***
Time for topKMatches("notarealword", 1) -  3.842946E-6
Time for topKMatches("notarealword", 4) -  1.74699E-6
Time for topKMatches("notarealword", 7) -  1.721601E-6

Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\fourletterwords.txt.
Benchmarking BinarySearchAutocomplete...
Found 456976 words
Time to initialize - 0.064207555
Time for topMatch("") - 0.02364848811792453
Time for topMatch("nenk") - 1.8723693E-5
Time for topMatch("n") - 9.04459524E-4
Time for topMatch("ne") - 3.0313572E-5
Time for topMatch("notarealword") - 1.8885211E-5
Time for topKMatches("", 1) -  0.05135780392857143
Time for topKMatches("", 4) -  0.05709344790909091
Time for topKMatches("", 7) -  0.057503291091954026
Time for topKMatches("nenk", 1) -  2.786865E-6
Time for topKMatches("nenk", 4) -  2.653436E-6
Time for topKMatches("nenk", 7) -  2.838723E-6
Time for topKMatches("n", 1) -  0.001614563436
Time for topKMatches("n", 4) -  0.001385317721
Time for topKMatches("n", 7) -  0.00111221737
Time for topKMatches("ne", 1) -  2.7870271E-5
Time for topKMatches("ne", 4) -  3.9051763E-5
***Time for topKMatches("ne", 7) -  3.2875716E-5***
Time for topKMatches("notarealword", 1) -  2.270439E-6
Time for topKMatches("notarealword", 4) -  9.93418E-7
Time for topKMatches("notarealword", 7) -  1.292146E-6

Doubling the source size does increases the runtime, but not double (about logN), but sometimes increasing the
source size actually shortened the runtime, these are marked with ***. Increasing the size of the prefix decreases
runtime because this decreases the amount of matches, so both  O(log N + M) and O(log N + M log M) will decrease.

4C TrieAutocomplete:

Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\fourletterwordshalf.txt.
Benchmarking TrieAutocomplete...
Found 228488 words
Time to initialize - 0.208114915
Created 237628 nodes
Time for topMatch("") - 8.590718E-6
Time for topMatch("aenk") - 1.061483E-6
Time for topMatch("a") - 9.540921E-6
Time for topMatch("ae") - 8.40057E-6
Time for topMatch("notarealword") - 3.6031E-7
Time for topKMatches("", 1) -  1.557803E-4
Time for topKMatches("", 4) -  1.15897718E-4
Time for topKMatches("", 7) -  1.37351015E-4
Time for topKMatches("aenk", 1) -  1.260275E-6
Time for topKMatches("aenk", 4) -  1.121445E-6
Time for topKMatches("aenk", 7) -  1.1652E-6
Time for topKMatches("a", 1) -  1.6424623E-5
Time for topKMatches("a", 4) -  4.3000048E-5
Time for topKMatches("a", 7) -  8.5632763E-5
Time for topKMatches("ae", 1) -  7.851192E-6
Time for topKMatches("ae", 4) -  2.4793321E-5
Time for topKMatches("ae", 7) -  4.4784311E-5
Time for topKMatches("notarealword", 1) -  5.56401E-7
Time for topKMatches("notarealword", 4) -  5.02921E-7
Time for topKMatches("notarealword", 7) -  3.8732E-7

Opening - C:\Users\Alex Boldt\Documents\JavaWorkspace\Autocomplete\data\fourletterwords.txt.
Benchmarking TrieAutocomplete...
Found 456976 words
Time to initialize - 2.247276338
Created 475255 nodes
Time for topMatch("") - 8.698757E-6
Time for topMatch("nenk") - 1.19329E-6
Time for topMatch("n") - 5.922156E-6
Time for topMatch("ne") - 2.997001E-6
Time for topMatch("notarealword") - 5.98536E-7
Time for topKMatches("", 1) -  5.1904619E-5
Time for topKMatches("", 4) -  5.7927791E-5
Time for topKMatches("", 7) -  9.4243468E-5
Time for topKMatches("nenk", 1) -  8.62691E-7
Time for topKMatches("nenk", 4) -  9.04826E-7
Time for topKMatches("nenk", 7) -  8.04891E-7
Time for topKMatches("n", 1) -  9.450169E-6
Time for topKMatches("n", 4) -  3.2015726E-5
Time for topKMatches("n", 7) -  5.4438673E-5
Time for topKMatches("ne", 1) -  6.712461E-6
Time for topKMatches("ne", 4) -  1.5283732E-5
Time for topKMatches("ne", 7) -  2.3864186E-5
Time for topKMatches("notarealword", 1) -  7.32504E-7
Time for topKMatches("notarealword", 4) -  6.63899E-7
Time for topKMatches("notarealword", 7) -  6.6498E-7


Doubling the source size doesn't affect search times too much, because the trie is navigated completely 
independently of other nodes in the system that are irrelevant to the prefix. Increasing the size of the prefix 
significantly cuts down on running time because many less nodes need be looked through if you are starting 
deeper in the trie (which is what a longer prefix corresponds to). 
