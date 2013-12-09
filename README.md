# raffer

Scans Twitter for a particular text within a tweet. Collects raffle participant screen names, and, by the end of the raffle, randomly picks a winner.

## What does it do exactly?

According to [Twitter APIs](https://dev.twitter.com/docs/api/1.1/get/search/tweets) and their [usage](https://dev.twitter.com/docs/using-search): Twitter "usually only serves tweets from the past week".

raffer scans for a desired text, that was chosen for the participants to include in their tweets to enter the raffle, and saves all the screen/names that it could find. 

It then wakes up a week later (in case a raffle takes longer than a week), and scans for new tweets. New tweets are appened to the existing ones.

It then wakes up a week later.... And so forth until the raffle is done (usually on a set date), after which raffer randomly chooses the winner out of all the participants.

## How is it done?

```clojure
(scan-for "tried @product from this @startup, and now can win an iPad (and so can you)")
```

would create a unique file with all the data that raffer found. raffer would grow this file later if more participants join the raffle.

### How to use it?

In order to use raffer, you'd need to tell raffer where to save resulting files, as well as specify your Twitter creds:

```clojure
{
 :path 
   {:players "path-to/raffer.players"} ;; this would be just a file prefix, e.g. the real file would look like: "raffer.players.2384768923"

 :twitter
   {:consumer-key ...
    :consumer-secret ...
    :access-token ...
    :access-token-secret ...}
}
```

raffer will look for a "raffer.conf" file, in the format above, from JVM params (e.g. -Draffer.conf=path).

## License

Copyright © 2013 tolitius

Distributed under the Eclipse Public License, the same as Clojure.
