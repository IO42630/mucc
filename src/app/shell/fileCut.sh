#!/bin/bash

# ================================================================================
#         FILE:  fileCut.sh
#
#        USAGE:  fileCut.sh [input file] [cut start] [cut end] [output file]
#
#  DESCRIPTION:  Cuts section between 'cut start' and 'cut end' from 'input file'
#                and writes the contents of the cut to 'output file'.
#
# ================================================================================

input_file=$1
start_cut=$2
end_cut=$3
output_file=$4
let cut_size=$end_cut-$start_cut


head -n $start_cut $input_file | tail -n 1 | sed 's/.*%PDF/%PDF/g' > $output_file


head -n $end_cut $input_file | tail -n $cut_size >> $output_file
