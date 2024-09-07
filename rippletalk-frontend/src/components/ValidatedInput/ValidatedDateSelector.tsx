import React, { useEffect, useState } from 'react';
import { determineValidatedSelectStyle } from '../../utils/DetermineStylesUtils';
import { StyledInputBox, StyledInputLabel } from './StyledInput';
import './ValidatedInput.css';
import { ExpandMoreRounded } from '@mui/icons-material';

interface ValidatedDateSelectorProps {
	style: string;
	valid: boolean;
	name: string;
	dropdown(): JSX.Element[];
	dispatcher(name: string, value: string | number | boolean): void;
	data?: number;
}

export const ValidatedDateSelector: React.FC<ValidatedDateSelectorProps> = ({
	style,
	valid,
	name,
	dropdown,
	dispatcher,
	data,
}) => {
	const [active, setActive] = useState<boolean>(false);
	const [value, setValue] = useState<number>(0);
	const [color, setColor] = useState<string>('gray');

	const changeValue = (e: React.ChangeEvent<HTMLSelectElement>) => {
		setValue(+e.target.value);
		dispatcher(name.toLowerCase(), +e.target.value);
	};

	const toggleActive = (e: React.FocusEvent<HTMLSelectElement>) => {
		setActive(!active);
	};

	useEffect(() => {
		setColor(determineValidatedSelectStyle(active, valid));
	}, [active, valid, value]);

	return (
		<div className='validated-input'>
			<StyledInputBox active={active} valid={valid}>
				<StyledInputLabel color={color} active={true} valid={valid}>
					{name}
					<ExpandMoreRounded
						sx={{
							fontSize: '34px',
							color: active ? '#1DA1F2' : '#657786',
							position: 'absolute',
							right: '15px',
							top: '35%',
						}}
					/>
				</StyledInputLabel>
				<select
					className='validated-input-value validated-date-selector'
					value={data}
					onChange={changeValue}
					onFocus={toggleActive}
					onBlur={toggleActive}
				>
					{dropdown()}
				</select>
			</StyledInputBox>
		</div>
	);
};
