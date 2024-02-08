##!/bin/sh

hosts=("grouper" "beaver-creek" "riyadh" "bugatti" "cockroach" "montpelier" "preying-mantis" "steamboat" "baton-rouge" "howelsen-hill" "sacramento")

directory="~/code/ds-hw1/build/classes/java/main"
#start and detach from session
tmux new-session -d -s tmux-session-name

for i in "${!hosts[@]}"; do
    if [ "$i" -eq 0 ]; then
        #first host, just ssh
        tmux send-keys -t tmux-session-name "ssh ${hosts[$i]}" C-m "cd $directory" C-m "clear" C-m
    else
        #split the window first, then SSH
        tmux split-window -t tmux-session-name
        tmux select-layout -t tmux-session-name tiled
        tmux send-keys -t tmux-session-name "ssh ${hosts[$i]}" C-m "cd $directory" C-m "clear" C-m
    fi
    
    if [ "${hosts[$i]}" = "sacramento" ]; then
        tmux send-keys -t tmux-session-name "java csx55.overlay.node.Registry" C-m
    else
        tmux send-keys -t tmux-session-name "sleep 2" C-m "java csx55.overlay.node.MessagingNode sacramento.cs.colostate.edu" C-m
    fi
done

sleep 5
tmux send-keys -t tmux-session-name "setup-overlay 5" C-m "send-overlay-link-weights" C-m "start 5000"

#attach to the session
tmux attach -t tmux-session-name
