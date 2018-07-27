kubectl create -f .
echo "Waiting for everything to come up"
while true ; do
  printf "."
  result=$(kubectl get pods | grep "redis-enterprise" | grep -c "Running")
  # We want a minimum of 3 pods, plus the extra header line
  # it would be more headache to parse it out than its worth
  if [ "$result"  -ge 3 ] ; then
      echo " "
      break
  fi
  sleep 1
done
