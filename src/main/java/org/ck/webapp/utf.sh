FILES=$(find . -type f -name '*.*')
for f in $FILES
do
    if test -f $f; then
        enca -d -x utf-8 $f
    else
        echo -e "\nSkipping $f - it's a regular file";
    fi
done
