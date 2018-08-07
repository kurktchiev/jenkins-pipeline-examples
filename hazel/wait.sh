while true ; do
  printf "."
  result=$(kubectl get pods | grep "demo-hazelcast-enterprise-" | grep -c "Running")
  if [ "$result"  -ge 4 ] ; then
      echo " "
      break
  fi
  sleep 1
done
